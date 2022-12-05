package com.example.apiservice.controller;

import com.example.apiservice.domain.dto.ResponseDto;
import com.example.apiservice.domain.dto.user.UpdatePasswordDto;
import com.example.apiservice.domain.dto.user.UserInfoDto;
import com.example.apiservice.domain.dto.user.UserRoomInfoDto;
import com.example.apiservice.domain.entity.User;
import com.example.apiservice.util.MockUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @GetMapping("/myinfo")
    public ResponseEntity<ResponseDto> getMyInfo() {
        // TODO
        User fakeUser = MockUtil.getFakeUser();
        UserInfoDto userInfoDto = new UserInfoDto().setUserId(fakeUser.getId())
                .setName(fakeUser.getName())
                .setGender(fakeUser.getGender())
                .setEmail(fakeUser.getEmail())
                .setTelephone(fakeUser.getTelephone())
                .setClassName("Class test")
                .setStudentId("2201xxxxx")
                .setLastLoginTime(LocalDateTime.now());
        ResponseDto responseDto = ResponseDto.ok().setMessage("User info").setData(userInfoDto);
        log.info(fakeUser.toString());

        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/passwd")
    public ResponseEntity<ResponseDto> updatePassword(@Valid @RequestBody UpdatePasswordDto updatePasswordDto) {
        // TODO

        return ResponseEntity.ok(ResponseDto.ok("Update password successfully"));
    }

    @GetMapping("/myroom")
    public ResponseEntity<ResponseDto> getMyRoom() {
        UserRoomInfoDto userRoomInfoDto = new UserRoomInfoDto().setRoomName("5001")
                .setMembers(MockUtil.getFakeNameList());

        return ResponseEntity.ok(ResponseDto.ok().setMessage("Room info").setData(userRoomInfoDto));
    }
}
