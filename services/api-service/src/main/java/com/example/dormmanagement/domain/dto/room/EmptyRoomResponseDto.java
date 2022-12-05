package com.example.dormmanagement.domain.dto.room;

import com.example.dormmanagement.domain.dto.IBaseDto;
import com.example.dormmanagement.type.enumration.Gender;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class EmptyRoomResponseDto implements IBaseDto {
    @JsonProperty("building_id")
    private Long buildingId;

    private Gender gender;

    @JsonProperty("cnt")
    private Long emptyRoomNumber;
}
