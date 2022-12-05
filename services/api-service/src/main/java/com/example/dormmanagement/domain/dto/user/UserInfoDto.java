package com.example.dormmanagement.domain.dto.user;

import com.example.dormmanagement.domain.dto.IBaseDto;
import com.example.dormmanagement.type.enumration.Gender;
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

    private Gender gender;

    private String email;

    @JsonProperty("tel")
    private String telephone;

    @JsonProperty("last_login_time")
    private LocalDateTime lastLoginTime;

    @JsonProperty("class_name")
    private String className;
}
