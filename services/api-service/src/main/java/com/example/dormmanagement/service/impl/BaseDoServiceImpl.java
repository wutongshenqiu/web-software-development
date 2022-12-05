package com.example.dormmanagement.service.impl;

import com.example.dormmanagement.dao.IBaseDao;
import com.example.dormmanagement.domain.entity.Base;
import com.example.dormmanagement.service.IBaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

@Slf4j
@Transactional
public abstract class BaseDoServiceImpl<T extends Base, I extends Serializable> implements IBaseService<T, I> {
    public abstract IBaseDao<T, I> getBaseDao();

    @Override
    public T find(I id) {
        return getBaseDao().findById(id).orElse(null);
    }

    @Override
    public List<T> findAll() {
        return getBaseDao().findAll();
    }

    @Override
    public boolean exists(I id) {
        return getBaseDao().findById(id).isPresent();
    }

    @Override
    public void save(T entity) {
        getBaseDao().save(entity);
    }

    @Override
    public void delete(I id) {
        getBaseDao().deleteById(id);
    }
}
