package com.example.dormmanagement.domain.dto.auth;

import com.example.dormmanagement.domain.dto.IBaseDto;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Accessors(chain = true)
@Data
public class DeviceInfoInRedisDto implements IBaseDto, Serializable {
    private String ip;

    private String refreshToken;
}
