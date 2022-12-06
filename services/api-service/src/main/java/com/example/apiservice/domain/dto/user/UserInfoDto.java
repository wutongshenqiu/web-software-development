package com.example.apiservice.domain.dto.user;

import com.example.apiservice.domain.dto.IBaseDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Accessors(chain = true)
@Data
public class UserInfoDto implements IBaseDto {
    @JsonProperty("uid")
    private Long userId;

    @JsonProperty("studentid")
    private String studentId;

    private String name;

    private Integer gender;

    private String email;

    @JsonProperty("tel")
    private String telephone;

    @JsonProperty("last_login_time")
    private LocalDateTime lastLoginTime;

    @JsonProperty("class_name")
    private String className;

    @JsonProperty("verification_code")
    private String verificationCode = "Useless";
}
