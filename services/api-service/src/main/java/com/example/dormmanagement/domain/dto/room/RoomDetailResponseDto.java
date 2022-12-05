package com.example.dormmanagement.domain.dto.room;

import com.example.dormmanagement.domain.dto.IBaseDto;
import com.example.dormmanagement.type.enumration.Gender;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class RoomDetailResponseDto implements IBaseDto {
    private String name;

    private Gender gender;

    @JsonProperty("describe")
    private String description;

    @JsonProperty("image_url")
    private String imageUrl;

    @JsonProperty("building_id")
    private Long buildingId;
}
