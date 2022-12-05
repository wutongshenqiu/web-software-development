package com.example.apiservice.domain.dto.user;

import com.example.apiservice.domain.dto.IBaseDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Accessors(chain = true)
@Data
public class UserRoomInfoDto implements IBaseDto {
    private String roomName;

    @JsonProperty("member")
    private List<String> members;
}
