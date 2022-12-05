package com.example.apiservice.domain.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter
@Setter
public class ResponseDto implements IBaseDto {
    private Integer code;
    private String message;
    private Object data;

    public static ResponseDto ok() {
        return new ResponseDto().setCode(200);
    }

    public static ResponseDto ok(String message) {
        return new ResponseDto().setCode(200).setMessage(message);
    }
}
