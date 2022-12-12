package com.example.apiservice.type.enumration;

import com.fasterxml.jackson.annotation.JsonValue;

public enum RoleStatus {
    ACTIVE, DELETED;

    @JsonValue
    public int toValue() {
        return ordinal();
    }
}
