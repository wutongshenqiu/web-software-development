package com.example.apiservice.dao;

import com.example.apiservice.domain.entity.SysConfig;
import org.springframework.stereotype.Repository;

@Repository
public interface ISysConfigDao extends IBaseDao<SysConfig, Long> {
}
