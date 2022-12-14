package com.example.apiservice.service;

import java.io.Serializable;
import java.util.List;

public interface IBaseService<T, I extends Serializable> {
    T find(I id);

    T findOrElseRaise(I id);

    List<T> findAll();

    boolean exists(I id);

    void delete(I id);

    void save(T entity);

    void saveAndFlush(T entity);
}
