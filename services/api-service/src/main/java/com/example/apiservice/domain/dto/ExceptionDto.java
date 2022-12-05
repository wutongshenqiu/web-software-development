package com.example.apiservice.domain.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class ExceptionDto {
    private Integer code;

    private String message;
}
