package com.example.apiservice.domain.dto.group;

import com.example.apiservice.domain.dto.IBaseDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class GroupMemberDto implements IBaseDto {
    @JsonProperty("student_id")
    private String studentId;

    @JsonProperty("student_name")
    private String studentName;
}
