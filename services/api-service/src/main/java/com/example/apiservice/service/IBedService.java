package com.example.apiservice.service;

import com.example.apiservice.domain.entity.Bed;

public interface IBedService extends IBaseService<Bed, Long> {
    Bed findByUserIdOrElseThrow(Long userId);
}
