package com.example.dormmanagement.util;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaFoxUtil;
import com.example.dormmanagement.domain.dto.auth.DeviceInfoInRedisDto;
import com.example.dormmanagement.domain.dto.auth.LoginTokenResponseDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthUtil {
    public static String getClientPreviousIpAddress() {
        DeviceInfoInRedisDto deviceInfoInRedisDto = getDeviceInfo();
        if (deviceInfoInRedisDto == null) return null;

        return deviceInfoInRedisDto.getIp();
    }

    public static String getClientRefreshToken() {
        DeviceInfoInRedisDto deviceInfoInRedisDto = getDeviceInfo();
        if (deviceInfoInRedisDto == null) return null;

        return deviceInfoInRedisDto.getRefreshToken();
    }

    public static void renewRefreshToken() {
        String refreshToken = SaFoxUtil.getRandomString(64);

        renewRefreshToken(refreshToken);
    }

    public static void renewRefreshToken(String refreshToken) {
        StpUtil.checkLogin();
        String device = StpUtil.getLoginDevice();
        SaSession session = StpUtil.getSession();

        DeviceInfoInRedisDto deviceInfoInRedisDto = (DeviceInfoInRedisDto) session.get(device);
        deviceInfoInRedisDto.setRefreshToken(refreshToken);

        session.set(device, deviceInfoInRedisDto);
    }

    public static DeviceInfoInRedisDto getDeviceInfo() {
        StpUtil.checkLogin();
        String device = StpUtil.getLoginDevice();
        SaSession session = StpUtil.getSession();

        return (DeviceInfoInRedisDto) session.get(device);
    }

    public static LoginTokenResponseDto getCurrentTokenDto() {
        String device = StpUtil.getLoginDevice();

        return new LoginTokenResponseDto()
                .setAccessToken(StpUtil.getTokenValue())
                .setExpiresIn(7200)
                .setTokenType("Bearer")
                .setScope(device)
                .setRefreshToken(getClientRefreshToken());
    }

    public static void login(Object id) {
        String device = HttpReqRespUtil.getClientDevice();
        String ip = HttpReqRespUtil.getClientIpAddress();

        if (StpUtil.isLogin() && StpUtil.getLoginDevice().equals(device)) {
            StpUtil.logout();
        }

        StpUtil.login(id, device);

        SaSession session = StpUtil.getSession();
        DeviceInfoInRedisDto deviceInfoInRedisDto = new DeviceInfoInRedisDto()
                .setIp(ip)
                .setRefreshToken(SaFoxUtil.getRandomString(64));
        session.set(device, deviceInfoInRedisDto);
    }

    public static void logout() {
        SaSession session = StpUtil.getSession();
        String device = StpUtil.getLoginDevice();
        log.info("Delete device: " + device);
        session.delete(device);

        StpUtil.logout();
    }
}
