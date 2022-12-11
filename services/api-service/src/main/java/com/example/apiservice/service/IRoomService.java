package com.example.apiservice.service;

import com.example.apiservice.domain.entity.Room;

public interface IRoomService extends IBaseService<Room, Long> {
    Room findRoomByUserId(Long userId);
}
