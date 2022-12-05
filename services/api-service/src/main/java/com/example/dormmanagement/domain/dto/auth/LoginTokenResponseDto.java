package com.example.dormmanagement.domain.dto.auth;

import com.example.dormmanagement.domain.dto.IBaseDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class LoginTokenResponseDto implements IBaseDto {
    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("token_type")
    private String tokenType = "bearer";

    @JsonProperty("expires_in")
    private Integer expiresIn;

    private String scope;

    @JsonProperty("refresh_token")
    private String refreshToken;
}
