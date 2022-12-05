package com.example.dormmanagement.service;

import com.example.dormmanagement.domain.dto.form.FormCreateDto;
import com.example.dormmanagement.domain.entity.Form;

public interface IFormService extends IBaseService<Form, Long> {
    void sendFormToMQ(FormCreateDto formCreateDto);
}
