package com.example.apiservice.domain.dto.group;

import com.example.apiservice.domain.dto.IBaseDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class GroupCreateResponseDto implements IBaseDto {
    @JsonProperty("team_id")
    private Long groupId;

    @JsonProperty("invite_code")
    private String inviteCode;
}
