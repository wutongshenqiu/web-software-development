package com.example.apiservice.dao;

import com.example.apiservice.domain.entity.Form;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IFormDao extends IBaseDao<Form, Long> {
    List<Form> findAllBySubmitterId(Long submitterId);
}
