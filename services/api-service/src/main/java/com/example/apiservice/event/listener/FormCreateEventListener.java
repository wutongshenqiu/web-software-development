package com.example.apiservice.event.listener;

import com.example.apiservice.domain.dto.form.FormInRabbitDto;
import com.example.apiservice.event.FormCreateEvent;
import com.example.apiservice.service.IFormService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class FormCreateEventListener {
    private final IFormService formService;

    public FormCreateEventListener(final IFormService formService) {
        this.formService = formService;
    }

    @TransactionalEventListener
    public void processFormCreateEvent(FormCreateEvent event) {
        FormInRabbitDto formInRabbitDto = event.getFormInRabbitDto();
        formService.sendFormToMQ(formInRabbitDto);
    }
}
