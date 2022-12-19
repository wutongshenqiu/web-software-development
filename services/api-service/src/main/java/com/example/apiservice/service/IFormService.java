package com.example.apiservice.service;

import com.example.apiservice.domain.dto.form.FormInRabbitDto;
import com.example.apiservice.domain.entity.Form;

import java.util.List;

public interface IFormService extends IBaseService<Form, Long> {
    void sendFormToMQ(FormInRabbitDto formCreateDto);

    List<Form> findAllBySubmitterId(Long submitterId);
    Form findBySubmitterId(Long submitterId, Long formId);

    Form submitForm(Long userId, Long groupId, Long buildingId);
}
