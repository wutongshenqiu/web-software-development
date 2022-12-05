package com.example.dormmanagement.domain.dto.auth;

import com.example.dormmanagement.domain.dto.IBaseDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class RefreshTokenDto implements IBaseDto {
    @JsonProperty("refresh_token")
    private String refreshToken;
}
