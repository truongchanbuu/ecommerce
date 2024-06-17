package com.buutruong.ecommerce.features.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateEmailRequest {
    @JsonProperty("new_email")
    @Email(message = "Invalid email")
    @NotNull(message = "Email should not be empty")
    @NotBlank(message = "Email should not be empty")
    private String newEmail;
}
