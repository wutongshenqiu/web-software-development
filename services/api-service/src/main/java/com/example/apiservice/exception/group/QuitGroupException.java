package com.example.apiservice.exception.group;

import com.example.apiservice.exception.IBaseException;

public class QuitGroupException extends RuntimeException implements IBaseException {
    public QuitGroupException(String message) {
        super(message);
    }

    @Override
    public Integer getCode() {
        return 514003;
    }
}
