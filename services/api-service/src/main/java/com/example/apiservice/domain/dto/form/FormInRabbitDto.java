package com.example.apiservice.domain.dto.form;

import com.example.apiservice.domain.dto.IBaseDto;
import com.example.apiservice.domain.entity.User;
import com.example.apiservice.type.enumration.Gender;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
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
    private List<UserDto> groupMembers;

    @Data
    @Accessors(chain = true)
    public static class UserDto {
        @JsonProperty("id")
        private Long id;

        private String name;

        private Gender gender;
    }

    public FormInRabbitDto setGroupMembersFromUsers(List<User> groupMembers) {
        List<UserDto> members = new ArrayList<>();

        for (User user : groupMembers) {
            UserDto member = new UserDto()
                    .setGender(user.getGender())
                    .setId(user.getId())
                    .setName(user.getName());
            members.add(member);
        }
        setGroupMembers(members);

        return this;
    }
}
