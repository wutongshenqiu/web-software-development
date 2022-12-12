package com.example.apiservice.type.enumration;

import com.fasterxml.jackson.annotation.JsonValue;

public enum FormStatus {
    CREATED, PROCESSING, FAILED, SUCCESS, DELETED;

//    @JsonValue
//    public int toValue() {
//        return ordinal();
//    }
}
