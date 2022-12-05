package com.example.dormmanagement.domain.dto.user;

import com.example.dormmanagement.domain.dto.IBaseDto;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class UpdatePasswordDto implements IBaseDto {
    private String oldPassword;

    private String newPassword;
}
