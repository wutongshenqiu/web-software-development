package com.example.apiservice.type.enumration;

import com.fasterxml.jackson.annotation.JsonValue;

public enum RoomStatus {
    AVAILABLE, UNAVAILABLE, DELETED;

    @JsonValue
    public int toValue() {
        return ordinal();
    }
}
