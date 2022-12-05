package com.example.dormmanagement.domain.dto.group;

import com.example.dormmanagement.domain.dto.IBaseDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class GroupCreateDto implements IBaseDto {
    private String name;

    @JsonProperty("describe")
    private String description;
}
