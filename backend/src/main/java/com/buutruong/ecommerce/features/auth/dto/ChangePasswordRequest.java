package com.buutruong.ecommerce.features.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChangePasswordRequest {
    @Size(min = 8, max = 20, message = "Password must be at least 8 characters")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$"
            , message = "Password must be 1 uppercase, 1 lowercase, 1 digit and 1 special character")
    private String password;
    @JsonProperty("confirmed_password")
    private String confirmed;
}
