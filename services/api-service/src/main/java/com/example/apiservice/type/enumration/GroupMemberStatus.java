package com.example.apiservice.type.enumration;

import com.fasterxml.jackson.annotation.JsonValue;

public enum GroupMemberStatus {
    JOINED, LEFT, DELETED;

    @JsonValue
    public int toValue() {
        return ordinal();
    }
}
