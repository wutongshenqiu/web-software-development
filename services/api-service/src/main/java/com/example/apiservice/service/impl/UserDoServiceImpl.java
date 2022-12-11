package com.example.apiservice.service.impl;

import com.example.apiservice.dao.IBaseDao;
import com.example.apiservice.dao.IStudentInfoDao;
import com.example.apiservice.dao.IUserDao;
import com.example.apiservice.domain.entity.Bed;
import com.example.apiservice.domain.entity.Room;
import com.example.apiservice.domain.entity.StudentInfo;
import com.example.apiservice.domain.entity.User;
import com.example.apiservice.service.IRoomService;
import com.example.apiservice.service.IUserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDoServiceImpl extends BaseDoServiceImpl<User, Long> implements IUserService {
    private final IUserDao userDao;
    private final IStudentInfoDao studentInfoDao;
    private final IRoomService roomService;

    public UserDoServiceImpl(final IUserDao userDao, final IStudentInfoDao studentInfoDao, final IRoomService roomService) {
        this.userDao = userDao;
        this.studentInfoDao = studentInfoDao;
        this.roomService = roomService;
    }

    @Override
    public IBaseDao<User, Long> getBaseDao() {
        return this.userDao;
    }

    @Override
    public StudentInfo getStudentInfo(Long userId) {
        return studentInfoDao.findStudentInfoByUserId(userId).orElse(null);
    }

    @Override
    public List<User> findRoomMembers(Long userId) {
        Room room = roomService.findRoomByUserId(userId);

        List<User> members = new ArrayList<>();
        for (Bed bed : room.getBeds()) {
            User user = bed.getUser();
            if (user != null && !user.getId().equals(userId)) {
                members.add(user);
            }
        }

        return members;
    }
}
