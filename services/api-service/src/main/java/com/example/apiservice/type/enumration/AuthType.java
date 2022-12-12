package com.example.apiservice.type.enumration;

import com.fasterxml.jackson.annotation.JsonValue;

public enum AuthType {
    USERNAME, STUDENT_ID, TELEPHONE, WECHAT_ID;

    @JsonValue
    public int toValue() {
        return ordinal();
    }
}
