package com.buutruong.ecommerce.features.auth.util;

import com.buutruong.ecommerce.features.auth.dto.RegisterRequest;
import com.buutruong.ecommerce.features.auth.model.EmailToken;
import com.buutruong.ecommerce.features.auth.model.User;
import com.buutruong.ecommerce.features.auth.service.EmailService;
import com.buutruong.ecommerce.features.auth.service.EmailTokenService;
import com.buutruong.ecommerce.features.auth.type.AuthProvider;
import com.buutruong.ecommerce.features.auth.type.Role;
import com.buutruong.ecommerce.features.auth.type.EmailType;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AuthUtil {
    private final EmailTokenService emailTokenService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public void sendVerifyEmail(EmailType type, String email, String oldEmail) {
        EmailToken emailConfirmToken = emailTokenService.getValidToken(email, type);
        String verifyToken = emailConfirmToken != null ? emailConfirmToken.getToken() : createAndSaveEmailToken(email, type);

        String verifyUrl = buildVerificationUrl(type, verifyToken, oldEmail);
        emailService.sendConfirmationEmail(type, email, verifyUrl);
    }

    public void sendNotificationEmail(EmailType type, String email) {
        emailService.sendNotificationEmail(type, email);
    }

    private String createAndSaveEmailToken(String email, EmailType type) {
        String token = UUID.randomUUID().toString();
        EmailToken emailToken = createEmailToken(token, email, type);
        emailTokenService.saveVerifyToken(emailToken);
        return token;
    }

    @Contract(pure = true)
    private @NotNull String buildVerificationUrl(@NotNull EmailType type, String token, String oldEmail) {
        String emailType = switch (type) {
            case VERIFY_EMAIL -> "verify-email";
            case RESET_PASSWORD -> "reset-password";
            case DELETE_ACCOUNT -> "delete";
            case UPDATE_EMAIL_ADDRESS -> "update-email";
        };

        String emailQueryParam = "";

        if (oldEmail != null) {
            emailQueryParam = "&oldEmail=" + oldEmail;
        }

        return "http://localhost:8080/api/v1/auth/" + emailType + "?token=" + token + emailQueryParam;
    }

    public boolean isValidToken(EmailToken token, EmailType type) {
        return token != null && token.getExpiredAt().isAfter(LocalDateTime.now()) && token.getType().equals(type);
    }

    public EmailToken createEmailToken(String token, String email, EmailType type) {
        return EmailToken.builder()
                .email(email)
                .token(token)
                .type(type)
                .createdAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusMinutes(15))
                .build();
    }

    public User createUser(@NotNull RegisterRequest request) {
        return User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .username(request.getUsername())
                .role(Role.CUSTOMER)
                .provider(AuthProvider.EMAIL)
                .phone(request.getPhoneNumber())
                .gender(request.getGender())
                .birthday(request.getBirthDate())
                .photoUrl(request.getPhotoUrl())
                .createdAt(LocalDateTime.now())
                .build();
    }
}

