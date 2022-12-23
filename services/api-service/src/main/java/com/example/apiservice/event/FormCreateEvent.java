package com.example.apiservice.event;

import com.example.apiservice.domain.dto.form.FormInRabbitDto;
import org.springframework.context.ApplicationEvent;

public class FormCreateEvent extends ApplicationEvent {
    private final FormInRabbitDto formInRabbitDto;

    public FormCreateEvent(Object source, final FormInRabbitDto formInRabbitDto) {
        super(source);
        this.formInRabbitDto = formInRabbitDto;
    }

    public FormInRabbitDto getFormInRabbitDto() {
        return formInRabbitDto;
    }
}
