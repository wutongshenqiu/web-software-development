package com.example.apiservice.service;

import com.example.apiservice.domain.entity.Building;

public interface IBuildingService extends IBaseService<Building, Long> {
    Integer getAvailableBedNumberByBuildingIdAndGender(Long buildingId, Integer gender);
}
