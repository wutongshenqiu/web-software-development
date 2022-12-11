package com.example.apiservice.dao;

import com.example.apiservice.domain.entity.Room;
import org.springframework.stereotype.Repository;

@Repository
public interface IRoomDao extends IBaseDao<Room, Long> {
}
