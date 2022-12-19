package com.example.apiservice.exception.group;

import com.example.apiservice.exception.IBaseException;

public class DeleteGroupException extends RuntimeException implements IBaseException {
    public DeleteGroupException(String message) {
        super(message);
    }

    @Override
    public Integer getCode() {
        return 514001;
    }
}
