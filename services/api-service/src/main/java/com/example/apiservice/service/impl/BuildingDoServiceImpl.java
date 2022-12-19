package com.example.apiservice.service.impl;

import com.example.apiservice.dao.IBaseDao;
import com.example.apiservice.dao.IBuildingDao;
import com.example.apiservice.domain.dto.room.EmptyRoomResponseDto;
import com.example.apiservice.domain.entity.Building;
import com.example.apiservice.service.IBuildingService;
import com.example.apiservice.type.enumration.Gender;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BuildingDoServiceImpl extends BaseDoServiceImpl<Building, Long> implements IBuildingService {
    private final IBuildingDao buildingDao;

    public BuildingDoServiceImpl(final IBuildingDao buildingDao) {
        this.buildingDao = buildingDao;
    }

    @Override
    public IBaseDao<Building, Long> getBaseDao() {
        return this.buildingDao;
    }

    @Override
    public Integer getAvailableBedNumberByBuildingIdAndGender(Long buildingId, Integer gender) {
        return buildingDao.countAvailableBedByBuildingIdAndGender(buildingId, gender);
    }

    @Override
    public List<EmptyRoomResponseDto> getAllAvailableBedNumbers() {
        List<Building> buildings = findAll();

        List<EmptyRoomResponseDto> emptyRoomResponseDtoList = new ArrayList<>();
        for (Building building : buildings) {
            emptyRoomResponseDtoList.add(new EmptyRoomResponseDto()
                    .setBuildingId(building.getId())
                    .setEmptyRoomNumber(getAvailableBedNumberByBuildingIdAndGender(building.getId(), Gender.MALE.ordinal()))
                    .setGender(Gender.MALE));
            emptyRoomResponseDtoList.add(new EmptyRoomResponseDto()
                    .setBuildingId(building.getId())
                    .setEmptyRoomNumber(getAvailableBedNumberByBuildingIdAndGender(building.getId(), Gender.FEMALE.ordinal()))
                    .setGender(Gender.FEMALE));
        }

        return emptyRoomResponseDtoList;
    }
}
