package com.buutruong.ecommerce.features.auth.dto;

import com.buutruong.ecommerce.features.auth.type.Gender;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class RegisterRequest {
    @Email(message = "Invalid email")
    @NotNull(message = "Email must not be null")
    private String email;
    @NotNull(message = "Please provide your user name")
    @NotBlank(message = "Please provide your user name")
    private String username;
    @Size(min = 8, max = 20, message = "Password must be at least 8 characters")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$"
            , message = "Password must be 1 uppercase, 1 lowercase, 1 digit and 1 special character")
    private String password;

    @Pattern(regexp = "^(https?|ftp)://[\\w-]+(\\.[\\w-]+)+([\\w.,@?^=%&:/~+#-]*[\\w@?^=%&/~+#-])?$")
    private String photoUrl;

    private LocalDate birthDate;
    @Pattern(regexp = "^\\+(?:[0-9] ?){6,14}[0-9]$")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
