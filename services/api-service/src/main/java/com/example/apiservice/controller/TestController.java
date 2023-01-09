package com.example.apiservice.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import com.example.apiservice.domain.dto.room.EmptyRoomResponseDto;
import com.example.apiservice.domain.entity.Auth;
import com.example.apiservice.domain.entity.StudentInfo;
import com.example.apiservice.domain.entity.User;
import com.example.apiservice.service.IAuthService;
import com.example.apiservice.service.IBuildingService;
import com.example.apiservice.service.IStudentService;
import com.example.apiservice.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@SaIgnore
@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    IUserService userService;

    @Autowired
    IAuthService authService;

    @Autowired
    IStudentService studentService;

    @Autowired
    IBuildingService buildingService;

    @GetMapping("/users")
    public List<?> getAllUsers() {
        log.warn("测试接口 `getAllUsers` 被调用，该接口应当仅用于测试使用");

        List<User> users = userService.findAll();
        Map<Long, Map<String, Object>> idToUserInfo = new HashMap<>();
        for (User user : users) {
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("id", user.getId());
            userInfo.put("name", user.getName());
            userInfo.put("gender", user.getGender());

            idToUserInfo.put(user.getId(), userInfo);
        }

        // will only return one password
        List<Auth> auths = authService.findAll();
        for (Auth auth : auths) {
            Long userId = auth.getUser().getId();
            idToUserInfo.get(userId).put("username", auth.getUsername());
            idToUserInfo.get(userId).put("password", auth.getPassword());
        }

        List<StudentInfo> studentInfos = studentService.findAll();
        for (StudentInfo studentInfo : studentInfos) {
            Long userId = studentInfo.getUser().getId();
            idToUserInfo.get(userId).put("student_id", studentInfo.getStudentId());
        }

        return Arrays.asList(idToUserInfo.values().toArray());
    }

    @GetMapping("/emptybeds")
    public List<EmptyRoomResponseDto> getAllEmptyRoom() {
        return buildingService.getAllAvailableBedNumbers();
    }
}
