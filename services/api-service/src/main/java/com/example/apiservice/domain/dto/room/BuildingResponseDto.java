package com.example.apiservice.domain.dto.room;

import com.example.apiservice.domain.dto.IBaseDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class BuildingResponseDto implements IBaseDto {
    @JsonProperty("building_id")
    private Long buildingId;

    @JsonProperty("building_name")
    private String buildingName;
}
