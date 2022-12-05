package com.example.dormmanagement.domain.dto.auth;

import com.example.dormmanagement.domain.dto.IBaseDto;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Accessors(chain = true)
@Data
public class LoginDto implements IBaseDto {
    @NotBlank(message = "The field is required")
    @Size(max = 50)
    private String username;

    @NotBlank(message = "The field is required")
    @Size(min = 6, message = "At least 6 characters")
    private String password;
}
