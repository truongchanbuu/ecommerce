package com.buutruong.ecommerce.features.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginRequest {
    @Email(message = "Email must not be null")
    private String email;

    @NotNull(message = "Password must not be null")
    private String password;
}
