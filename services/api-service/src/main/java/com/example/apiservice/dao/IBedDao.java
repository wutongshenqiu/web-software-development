package com.example.apiservice.dao;

import com.example.apiservice.domain.entity.Bed;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IBedDao extends IBaseDao<Bed, Long> {
    Optional<Bed> findBedByUserId(Long id);
}
