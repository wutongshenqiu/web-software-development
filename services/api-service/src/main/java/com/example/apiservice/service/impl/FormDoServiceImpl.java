package com.example.apiservice.service.impl;

import com.example.apiservice.dao.IBaseDao;
import com.example.apiservice.dao.IFormDao;
import com.example.apiservice.domain.dto.form.FormInRabbitDto;
import com.example.apiservice.domain.entity.Form;
import com.example.apiservice.service.IFormService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FormDoServiceImpl extends BaseDoServiceImpl<Form, Long> implements IFormService {
    private final IFormDao formDao;
    private final RabbitTemplate rabbitTemplate;

    public FormDoServiceImpl(final IFormDao formDao, final RabbitTemplate rabbitTemplate) {
        this.formDao = formDao;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public IBaseDao<Form, Long> getBaseDao() {
        return this.formDao;
    }

    @Override
    public void sendFormToMQ(FormInRabbitDto formInRabbitDto) {
        rabbitTemplate.convertAndSend("order", formInRabbitDto);
    }

    @Override
    public List<Form> findAllBySubmitterId(Long submitterId) {
        return formDao.findAllBySubmitterId(submitterId);
    }
}
