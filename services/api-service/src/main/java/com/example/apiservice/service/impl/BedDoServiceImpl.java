package com.example.apiservice.service.impl;

import com.example.apiservice.dao.IBaseDao;
import com.example.apiservice.dao.IBedDao;
import com.example.apiservice.domain.entity.Bed;
import com.example.apiservice.exception.ResourceNotFoundException;
import com.example.apiservice.service.IBedService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BedDoServiceImpl extends BaseDoServiceImpl<Bed, Long> implements IBedService {
    private final IBedDao bedDao;

    public BedDoServiceImpl(final IBedDao bedDao) {
        this.bedDao = bedDao;
    }

    @Override
    public IBaseDao<Bed, Long> getBaseDao() {
        return this.bedDao;
    }

    @Override
    public Bed findByUserIdOrElseThrow(Long userId) {
        Optional<Bed> t = bedDao.findBedByUserId(userId);
        if (t.isEmpty()) {
            throw new ResourceNotFoundException("用户 `" + userId + "` 不存在床位");
        }

        return t.get();
    }
}
