package com.example.apiservice.domain.dto.group;

import com.example.apiservice.domain.dto.IBaseDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class GroupJoinDto implements IBaseDto {
    @JsonProperty("invite_code")
    private String inviteCode;
}
