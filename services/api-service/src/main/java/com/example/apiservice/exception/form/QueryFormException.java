package com.example.apiservice.exception.form;

import com.example.apiservice.exception.IBaseException;

public class QueryFormException extends RuntimeException implements IBaseException {
    public QueryFormException(String message) {
        super(message);
    }

    @Override
    public Integer getCode() {
        return 515001;
    }
}
