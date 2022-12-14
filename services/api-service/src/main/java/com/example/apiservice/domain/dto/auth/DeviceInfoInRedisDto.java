package com.example.apiservice.domain.dto.auth;

import com.example.apiservice.domain.dto.IBaseDto;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Accessors(chain = true)
@Data
public class DeviceInfoInRedisDto implements IBaseDto, Serializable {
    private String ip;

    private String refreshToken;
}
