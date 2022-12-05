package com.example.dormmanagement.domain.dto.form;

import com.example.dormmanagement.domain.dto.IBaseDto;
import com.example.dormmanagement.type.enumration.FormStatus;
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
