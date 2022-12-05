package com.example.dormmanagement.domain.dto.form;

import com.example.dormmanagement.domain.dto.IBaseDto;
import com.example.dormmanagement.type.enumration.FormStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Accessors(chain = true)
@Data
public class FormDetailDto implements IBaseDto {
    @JsonProperty("order_id")
    private Long formId;

    @JsonProperty("group_name")
    private String groupName;

    @JsonProperty("building_name")
    private String buildingName;

    @JsonProperty("submit_time")
    private LocalDateTime submitTime;

    @JsonProperty("result_content")
    private String resultContent;

    private FormStatus status;
}
