package com.example.apiservice.dao;

import com.example.apiservice.domain.entity.Form;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IFormDao extends IBaseDao<Form, Long> {
    List<Form> findAllBySubmitterId(Long submitterId);
    Optional<Form> findBySubmitterIdAndId(Long submitterId, Long id);
}
