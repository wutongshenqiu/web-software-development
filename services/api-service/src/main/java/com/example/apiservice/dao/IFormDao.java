package com.example.apiservice.dao;

import com.example.apiservice.domain.entity.Form;
import org.springframework.stereotype.Repository;

@Repository
public interface IFormDao extends IBaseDao<Form, Long> {
}
