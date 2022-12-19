package com.example.apiservice.exception.group;

import com.example.apiservice.exception.IBaseException;

public class JoinGroupException extends RuntimeException implements IBaseException {
    public JoinGroupException(String message) {
        super(message);
    }

    @Override
    public Integer getCode() {
        return 514002;
    }
}
