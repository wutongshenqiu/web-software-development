package com.example.apiservice.service.impl;

import com.example.apiservice.dao.IBaseDao;
import com.example.apiservice.domain.entity.Base;
import com.example.apiservice.exception.ResourceNotFoundException;
import com.example.apiservice.service.IBaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Slf4j
@Transactional
public abstract class BaseDoServiceImpl<T extends Base, I extends Serializable> implements IBaseService<T, I> {
    public abstract IBaseDao<T, I> getBaseDao();

    @Override
    public T find(I id) {
        return getBaseDao().findById(id).orElse(null);
    }

    @Override
    public T findOrElseRaise(I id) throws ResourceNotFoundException {
        Optional<T> t = getBaseDao().findById(id);
        if (t.isEmpty()) {
            throw new ResourceNotFoundException("ID `" + id + "` 不存在");
        }

        return t.get();
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

    @Override
    public void saveAndFlush(T entity) {
        getBaseDao().saveAndFlush(entity);
    }
}
