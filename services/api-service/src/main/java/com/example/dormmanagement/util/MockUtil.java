package com.example.dormmanagement.util;

import cn.dev33.satoken.util.SaFoxUtil;
import com.example.dormmanagement.domain.entity.Building;
import com.example.dormmanagement.domain.entity.User;
import com.example.dormmanagement.type.enumration.BuildingStatus;
import com.example.dormmanagement.type.enumration.Gender;
import com.example.dormmanagement.type.enumration.UserStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MockUtil {
    public static User getFakeUser() {
        return User.builder()
                .name(getFakeName())
                .gender(Gender.FEMALE)
                .email("qiufeng@qiufeng.com.cn")
                .telephone("123456")
                .userStatus(UserStatus.ACTIVE)
                .id(1000L)
                .updateTime(LocalDateTime.now())
                .createTime(LocalDateTime.now())
                .build();
    }

    public static Building getFakeBuilding() {
        return Building.builder()
                .name("5号楼")
                .description("这是 5 号楼")
                .imageUrl("https://xxx.xxx.com")
                .buildingStatus(BuildingStatus.AVAILABLE)
                .id(10L)
                .updateTime(LocalDateTime.now())
                .createTime(LocalDateTime.now())
                .build();
    }

    public static String getFakeName() {
        return "qiufeng";
    }

    public static List<String> getFakeNameList() {
        List<String> nameList = new ArrayList<>();
        nameList.add("qiufeng");
        nameList.add("dongxue");
        nameList.add("xiayang");

        return nameList;
    }

    public static String getRandomString(int length) {
        return SaFoxUtil.getRandomString(length);
    }
}
