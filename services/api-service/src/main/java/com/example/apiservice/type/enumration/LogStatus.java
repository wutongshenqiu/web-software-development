package com.example.apiservice.type.enumration;

import com.fasterxml.jackson.annotation.JsonValue;

public enum LogStatus {
    SUCCESS, FAILED, DELETED;

    @JsonValue
    public int toValue() {
        return ordinal();
    }
}
