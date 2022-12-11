package com.example.apiservice.domain.dto.user;

import com.example.apiservice.domain.dto.IBaseDto;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

@Accessors(chain = true)
@Data
public class UserRoomInfoDto implements IBaseDto {
    private String roomName;

    private Long roomId;

    private Map<String, Object> member;
}
