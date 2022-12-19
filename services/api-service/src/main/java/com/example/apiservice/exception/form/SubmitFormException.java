package com.example.apiservice.exception.form;

import com.example.apiservice.exception.IBaseException;

public class SubmitFormException extends RuntimeException implements IBaseException {
    public SubmitFormException(String message) {
        super(message);
    }

    @Override
    public Integer getCode() {
        return 515000;
    }
}
