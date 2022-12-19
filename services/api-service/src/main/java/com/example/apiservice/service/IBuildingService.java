package com.example.apiservice.service;

import com.example.apiservice.domain.dto.room.EmptyRoomResponseDto;
import com.example.apiservice.domain.entity.Building;

import java.util.List;

public interface IBuildingService extends IBaseService<Building, Long> {
    Integer getAvailableBedNumberByBuildingIdAndGender(Long buildingId, Integer gender);

    List<EmptyRoomResponseDto> getAllAvailableBedNumbers();
}
