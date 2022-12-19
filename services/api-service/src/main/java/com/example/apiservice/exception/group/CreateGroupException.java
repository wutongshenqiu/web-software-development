package com.example.apiservice.exception.group;

import com.example.apiservice.exception.IBaseException;

public class CreateGroupException extends RuntimeException implements IBaseException {
    public CreateGroupException(String message) {
        super(message);
    }

    @Override
    public Integer getCode() {
        return 514000;
    }
}
