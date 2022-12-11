package com.example.apiservice.service.impl;

import com.example.apiservice.dao.IBaseDao;
import com.example.apiservice.dao.IRoomDao;
import com.example.apiservice.domain.entity.Room;
import com.example.apiservice.service.IBedService;
import com.example.apiservice.service.IRoomService;
import org.springframework.stereotype.Service;

@Service
public class RoomDoServiceImpl extends BaseDoServiceImpl<Room, Long> implements IRoomService {
    private final IRoomDao roomDao;
    private final IBedService bedService;

    public RoomDoServiceImpl(final IRoomDao roomDao, final IBedService bedService) {
        this.roomDao = roomDao;
        this.bedService = bedService;
    }

    @Override
    public IBaseDao<Room, Long> getBaseDao() {
        return this.roomDao;
    }

    @Override
    public Room findRoomByUserId(Long userId) {
        return bedService.findByUserIdOrElseThrow(userId).getRoom();
    }
}
