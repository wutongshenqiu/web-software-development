package com.example.apiservice.type.enumration;

import com.fasterxml.jackson.annotation.JsonValue;

public enum SysConfigStatus {
    ENABLE, DISABLE, DELETED;

    @JsonValue
    public int toValue() {
        return ordinal();
    }
}
