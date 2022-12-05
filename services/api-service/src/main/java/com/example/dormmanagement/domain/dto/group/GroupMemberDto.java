package com.example.dormmanagement.domain.dto.group;

import com.example.dormmanagement.domain.dto.IBaseDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class GroupMemberDto implements IBaseDto {
    @JsonProperty("student_id")
    private Long studentId;

    @JsonProperty("student_name")
    private String studentName;
}
