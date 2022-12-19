package com.example.apiservice.exception;

public class ResourceNotFoundException extends RuntimeException implements IBaseException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public Integer getCode() {
        return 510000;
    }
}
