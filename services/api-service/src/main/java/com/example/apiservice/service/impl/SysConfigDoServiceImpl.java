package com.example.apiservice.service.impl;

import com.example.apiservice.dao.IBaseDao;
import com.example.apiservice.dao.ISysConfigDao;
import com.example.apiservice.domain.entity.SysConfig;
import com.example.apiservice.service.ISysConfigService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SysConfigDoServiceImpl extends BaseDoServiceImpl<SysConfig, Long> implements ISysConfigService {
    private final ISysConfigDao sysConfigDao;

    public SysConfigDoServiceImpl(final ISysConfigDao sysConfigDao) {
        this.sysConfigDao = sysConfigDao;
    }

    @Override
    public IBaseDao<SysConfig, Long> getBaseDao() {
        return this.sysConfigDao;
    }

    @Override
    public LocalDateTime getStartTime() {
        return LocalDateTime.now();
    }

    @Override
    public LocalDateTime getEndTime() {
        return LocalDateTime.now().plusDays(1);
    }

    @Override
    public Integer getClassLimit() {
        return 0;
    }

    @Override
    public Integer getGroupLimit() {
        return 0;
    }

    @Override
    public Integer getGroupNumber() {
        return 4;
    }
}
