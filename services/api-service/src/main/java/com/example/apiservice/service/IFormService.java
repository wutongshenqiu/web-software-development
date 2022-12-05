package com.example.apiservice.service;

import com.example.apiservice.domain.dto.form.FormCreateDto;
import com.example.apiservice.domain.entity.Form;

public interface IFormService extends IBaseService<Form, Long> {
    void sendFormToMQ(FormCreateDto formCreateDto);
}
