package com.example.apiservice.type.enumration;

import com.fasterxml.jackson.annotation.JsonValue;

public enum BedStatus {
    AVAILABLE, UNAVAILABLE, OCCUPIED, DELETED;

    @JsonValue
    public int toValue() {
        return ordinal();
    }
}
