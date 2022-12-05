package com.example.dormmanagement.domain.dto.form;

import com.example.dormmanagement.domain.dto.IBaseDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class FormCreateResponseDto implements IBaseDto {
    @JsonProperty("order_id")
    private Long formId;
}
