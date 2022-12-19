package com.example.apiservice.exception.group;

import com.example.apiservice.exception.IBaseException;

public class QueryGroupException extends RuntimeException implements IBaseException {
    public QueryGroupException(String message) {
        super(message);
    }

    @Override
    public Integer getCode() {
        return 514004;
    }
}
