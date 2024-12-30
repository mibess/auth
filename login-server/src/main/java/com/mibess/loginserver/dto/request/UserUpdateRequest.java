package com.mibess.loginserver.dto.request;

import com.mibess.loginserver.entity.enums.Role;
import jakarta.validation.constraints.*;
import org.springframework.validation.annotation.Validated;

@Validated
public record UserUpdateRequest(
        @NotBlank(message = "Name cannot be blank")
        @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
        String name,

        @NotBlank(message = "Last name cannot be blank")
        @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
        String lastName,

        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Invalid email format")
        String email,

        @NotNull(message = "Role cannot be null")
        Role role
){}
