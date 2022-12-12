package com.example.apiservice.type.enumration;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Gender {
    FEMALE, MALE, UNKNOWN;

    @JsonValue
    public int toValue() {
        return ordinal();
    }
}
