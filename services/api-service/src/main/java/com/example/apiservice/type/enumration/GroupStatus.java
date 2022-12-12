package com.example.apiservice.type.enumration;

import com.fasterxml.jackson.annotation.JsonValue;

public enum GroupStatus {
    ACTIVE, COMPLETED, DELETED;

    @JsonValue
    public int toValue() {
        return ordinal();
    }
}
