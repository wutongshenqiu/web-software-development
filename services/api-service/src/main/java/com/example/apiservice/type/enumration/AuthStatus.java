package com.example.apiservice.type.enumration;

import com.fasterxml.jackson.annotation.JsonValue;

public enum AuthStatus {
    ACTIVE, DENIED, DELETED;

    @JsonValue
    public int toValue() {
        return ordinal();
    }
}
