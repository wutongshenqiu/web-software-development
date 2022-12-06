package com.example.apiservice.util;

import cn.dev33.satoken.util.SaFoxUtil;
import com.example.apiservice.domain.entity.Building;
import com.example.apiservice.domain.entity.User;
import com.example.apiservice.type.enumration.BuildingStatus;
import com.example.apiservice.type.enumration.Gender;
import com.example.apiservice.type.enumration.UserStatus;

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
                .imageUrl("https://thumbs.dreamstime.com/z/ramoji-film-city-hyderabad-inside-view-dummy-buildings-film-shooting-ramoji-film-city-amusement-park-dummy-buildings-film-135760408.jpg")
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
