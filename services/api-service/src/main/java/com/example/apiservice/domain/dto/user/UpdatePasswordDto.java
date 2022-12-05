package com.example.apiservice.domain.dto.user;

import com.example.apiservice.domain.dto.IBaseDto;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class UpdatePasswordDto implements IBaseDto {
    private String oldPassword;

    private String newPassword;
}
