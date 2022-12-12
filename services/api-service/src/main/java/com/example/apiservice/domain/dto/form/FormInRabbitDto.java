package com.example.apiservice.domain.dto.form;

import com.example.apiservice.domain.dto.IBaseDto;
import com.example.apiservice.domain.entity.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Accessors(chain = true)
@Data
public class FormInRabbitDto implements IBaseDto {
    @JsonProperty("order_id")
    private Long formId;

    @JsonProperty("building_id")
    private Long buildingId;

    @JsonProperty("group_id")
    private Long groupId;

    @JsonProperty("group_members")
    private List<User> groupMembers;
}
