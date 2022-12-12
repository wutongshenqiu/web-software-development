package com.example.apiservice.service.impl;

import com.example.apiservice.dao.IBaseDao;
import com.example.apiservice.dao.IBuildingDao;
import com.example.apiservice.domain.entity.Building;
import com.example.apiservice.service.IBuildingService;
import org.springframework.stereotype.Service;

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
}
