package com.example.apiservice.service.impl;

import com.example.apiservice.dao.IBaseDao;
import com.example.apiservice.dao.IFormDao;
import com.example.apiservice.domain.dto.form.FormCreateDto;
import com.example.apiservice.domain.entity.Form;
import com.example.apiservice.service.IFormService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class FormServiceImpl extends BaseDoServiceImpl<Form, Long> implements IFormService {
    private final IFormDao formDao;
    private final RabbitTemplate rabbitTemplate;

    public FormServiceImpl(final IFormDao formDao, RabbitTemplate rabbitTemplate) {
        this.formDao = formDao;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public IBaseDao<Form, Long> getBaseDao() {
        return this.formDao;
    }

    @Override
    public void sendFormToMQ(FormCreateDto formCreateDto) {
        Map<String, Long> data = new HashMap<>();
        data.put("building_id", formCreateDto.getBuildingId());
        data.put("group_id", formCreateDto.getGroupId());

        rabbitTemplate.convertAndSend("order", data);
    }
}
