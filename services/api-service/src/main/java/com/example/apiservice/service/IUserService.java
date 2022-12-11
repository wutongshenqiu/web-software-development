package com.example.apiservice.service;

import com.example.apiservice.domain.entity.StudentInfo;
import com.example.apiservice.domain.entity.User;

import java.util.List;

public interface IUserService extends IBaseService<User, Long> {
    StudentInfo getStudentInfo(Long userId);
    List<User> findRoomMembers(Long userId);
}
