package com.example.apiservice.domain.dto.form;

import com.example.apiservice.domain.dto.IBaseDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class FormCreateDto implements IBaseDto {
    @JsonProperty("group_id")
    private Long groupId;

    @JsonProperty("building_id")
    private Long buildingId;
}
