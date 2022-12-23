package com.example.apiservice.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.example.apiservice.domain.dto.ResponseDto;
import com.example.apiservice.domain.dto.user.UpdatePasswordDto;
import com.example.apiservice.domain.dto.user.UserInfoDto;
import com.example.apiservice.domain.dto.user.UserRoomInfoDto;
import com.example.apiservice.domain.entity.Room;
import com.example.apiservice.domain.entity.StudentInfo;
import com.example.apiservice.domain.entity.User;
import com.example.apiservice.service.IRoomService;
import com.example.apiservice.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    IUserService userService;

    @Autowired
    IRoomService roomService;

    @GetMapping("/myinfo")
    public ResponseEntity<ResponseDto> getMyInfo() {
        User user = userService.findOrElseRaise(StpUtil.getLoginIdAsLong());

        StudentInfo studentInfo = userService.getStudentInfo(user.getId());
        // TODO: 需要支持处理其他用户类型
        if (studentInfo == null) {
            throw new RuntimeException("不支持的用户类型");
        }

        UserInfoDto userInfoDto = new UserInfoDto().setUserId(user.getId())
                .setName(user.getName())
                .setGender(user.getGender().ordinal())
                .setEmail(user.getEmail())
                .setTelephone(user.getTelephone())
                .setClassName("测试班级")
                .setStudentId(studentInfo.getStudentId())
                .setLastLoginTime(LocalDateTime.now());
        // TODO: must execute after save to db
        ResponseDto responseDto = ResponseDto.ok().setMessage("个人信息").setData(userInfoDto);

        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/passwd")
    public ResponseEntity<ResponseDto> updatePassword(@Valid @RequestBody UpdatePasswordDto updatePasswordDto) {
        // TODO: 需要添加 auth 的类型，否则不足以对密码进行修改

        return ResponseEntity.ok(ResponseDto.ok("操作成功"));
    }

    @GetMapping("/myroom")
    public ResponseEntity<ResponseDto> getMyRoom() {
        User user = userService.findOrElseRaise(StpUtil.getLoginIdAsLong());
        List<User> members = userService.findRoomMembers(user.getId());

        List<Map<String, String>> memberDatas = new ArrayList<>();
        for (User member : members) {
            Map<String, String> memberData = new HashMap<>();
            memberData.put("name", member.getName());
            memberDatas.add(memberData);
        }

        Map<String, Object> data = new HashMap<>();
        data.put("rows", memberDatas);

        Room room = roomService.findRoomByUserId(user.getId());

        UserRoomInfoDto userRoomInfoDto = new UserRoomInfoDto()
                .setRoomId(room.getId())
                .setRoomName(room.getName())
                .setMember(data);

        return ResponseEntity.ok(ResponseDto.ok().setMessage("Room info").setData(userRoomInfoDto));
    }
}
