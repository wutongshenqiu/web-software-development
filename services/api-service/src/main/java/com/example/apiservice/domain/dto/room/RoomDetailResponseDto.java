package com.example.apiservice.domain.dto.room;

import com.example.apiservice.domain.dto.IBaseDto;
import com.example.apiservice.type.enumration.Gender;
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
