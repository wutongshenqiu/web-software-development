package com.example.apiservice.controller;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.session.TokenSign;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.example.apiservice.domain.dto.ResponseDto;
import com.example.apiservice.domain.dto.auth.*;
import com.example.apiservice.util.AuthUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Vector;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {
    @PostMapping(value = "/login")
    public ResponseEntity<ResponseDto> login(@Valid @RequestBody LoginDto loginDto) {
        // FIXME: just for test
        if (loginDto.getUsername().equals("qiufeng") && loginDto.getPassword().equals("123456")) {
            AuthUtil.login(10001);

            return ResponseEntity.ok(ResponseDto.ok().setMessage("操作成功").setData(AuthUtil.getCurrentTokenDto()));
        }

        return new ResponseEntity<>(ResponseDto.ok().setMessage("用户名或密码错误"), HttpStatus.UNAUTHORIZED);
    }

    @PostMapping(value = "/refreshtoken")
    public ResponseEntity<ResponseDto> refreshToken(@Valid @RequestBody RefreshTokenDto refreshTokenDto) {
        if (!refreshTokenDto.getRefreshToken().equals(AuthUtil.getClientRefreshToken()))
            return new ResponseEntity<>(ResponseDto.ok().setMessage("非法的 Token"), HttpStatus.UNAUTHORIZED);

        StpUtil.logoutByTokenValue(StpUtil.getTokenValue());
        AuthUtil.login(10001);

        LoginTokenResponseDto loginTokenResponseDto = AuthUtil.getCurrentTokenDto();

        return ResponseEntity.ok(ResponseDto.ok().setMessage("操作成功").setData(loginTokenResponseDto));
    }

    @GetMapping(value = "/logout")
    public ResponseEntity<ResponseDto> logout() {
        AuthUtil.logout();
        return ResponseEntity.ok(ResponseDto.ok("操作成功"));
    }

    @GetMapping(value = "/getToken")
    public SaResult getToken() {
        SaSession session = StpUtil.getSession();
        String device = StpUtil.getLoginDevice();
        DeviceInfoInRedisDto deviceInfoInRedisDto = AuthUtil.getDeviceInfo();

        TokenResponseDto tokenResponseDto = new TokenResponseDto();
        tokenResponseDto.setDevice(device)
                .setIp(deviceInfoInRedisDto.getIp())
                .setRefreshToken(deviceInfoInRedisDto.getRefreshToken())
                .setAccessToken(StpUtil.getTokenValue());

        return SaResult.ok("当前登录信息").setData(tokenResponseDto);
    }

    @GetMapping(value = "/getTokenList")
    public SaResult getTokenList() {
        SaSession session = StpUtil.getSession();
        List<TokenSign> tokenSignList = session.getTokenSignList();

        List<TokenResponseDto> tokenResponseDtoList = new Vector<>();
        for (TokenSign tokenSign : tokenSignList) {
            String tokenValue = tokenSign.getValue();
            DeviceInfoInRedisDto deviceInfoInRedisDto = AuthUtil.getDeviceInfo();
            TokenResponseDto tokenResponseDto = new TokenResponseDto();
            tokenResponseDto.setAccessToken(tokenValue)
                    .setRefreshToken(deviceInfoInRedisDto.getRefreshToken())
                    .setDevice(tokenSign.getDevice())
                    .setIp(deviceInfoInRedisDto.getIp());
            tokenResponseDtoList.add(tokenResponseDto);
        }
        SaResult saResult = SaResult.ok("登陆列表");
        saResult.setData(tokenResponseDtoList);

        return saResult;
    }

    @PostMapping(value = "/kickOutByToken")
    public SaResult kickOutByToken(@RequestBody String tokenValue) {
        log.info("Token: " + tokenValue);

        SaSession session = StpUtil.getSession();
        TokenSign tokenSign = session.getTokenSign(tokenValue);

        if (tokenSign == null) {
            SaResult saResult = SaResult.code(404);
            saResult.setMsg("Token 不存在");
            return saResult;
        }
        StpUtil.kickoutByTokenValue(tokenValue);

        return SaResult.ok(tokenSign.getDevice() + " 已踢下线");
    }

    @GetMapping("/kickOutRemaining")
    public SaResult KickOutRemaining() {
        StpUtil.checkLogin();
        SaSession session = StpUtil.getSession();
        String device = StpUtil.getLoginDevice();

        for (TokenSign tokenSign : session.getTokenSignList()) {
            if (!device.equals(tokenSign.getDevice())) {
                String tokenValue = tokenSign.getValue();
                StpUtil.kickoutByTokenValue(tokenValue);
                log.info(tokenSign.getDevice() + " 已踢下线");
            }
        }

        return SaResult.ok("剩余设备已经踢下线");
    }
}
