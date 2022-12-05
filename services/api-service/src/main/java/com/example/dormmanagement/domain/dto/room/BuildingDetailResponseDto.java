package com.example.dormmanagement.domain.dto.room;

import com.example.dormmanagement.domain.dto.IBaseDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class BuildingDetailResponseDto implements IBaseDto {
    private String name;

    @JsonProperty("describe")
    private String description;

    @JsonProperty("image_url")
    private String imageUrl;
}
