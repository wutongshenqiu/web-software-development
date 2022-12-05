package com.example.apiservice.domain.dto.group;

import com.example.apiservice.domain.dto.IBaseDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Accessors(chain = true)
@Data
public class GroupDetailDto implements IBaseDto {
    @JsonProperty("group_id")
    private Long groupId;

    @JsonProperty("group_name")
    private String groupName;

    @JsonProperty("invite_code")
    private String inviteCode;

    private List<GroupMemberDto> members;
}
