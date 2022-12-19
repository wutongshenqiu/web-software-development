package com.example.apiservice.exception.group;

import com.example.apiservice.exception.IBaseException;

public class TransferGroupException extends RuntimeException implements IBaseException {
    public TransferGroupException(String message) {
        super(message);
    }

    @Override
    public Integer getCode() {
        return 514005;
    }
}
