package com.example.apiservice.type.enumration;

import com.fasterxml.jackson.annotation.JsonValue;

public enum UserStatus {
    ACTIVE, DENIED, DELETED;

    @JsonValue
    public int toValue() {
        return ordinal();
    }
}
