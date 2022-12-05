package com.example.apiservice.domain.dto.form;

import com.example.apiservice.domain.dto.IBaseDto;
import com.example.apiservice.type.enumration.FormStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class FormInfoDto implements IBaseDto {
    private FormStatus status;

    @JsonProperty("room_id")
    private Long roomId;
}
