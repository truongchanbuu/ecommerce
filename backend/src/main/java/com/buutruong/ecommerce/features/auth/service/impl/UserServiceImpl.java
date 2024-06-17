package com.buutruong.ecommerce.features.auth.service.impl;

import com.buutruong.ecommerce.features.auth.dto.ChangePasswordRequest;
import com.buutruong.ecommerce.features.auth.model.CustomUserPrincipal;
import com.buutruong.ecommerce.features.auth.model.EmailToken;
import com.buutruong.ecommerce.features.auth.model.User;
import com.buutruong.ecommerce.features.auth.repository.UserRepository;
import com.buutruong.ecommerce.features.auth.service.EmailTokenService;
import com.buutruong.ecommerce.features.auth.service.UserService;
import com.buutruong.ecommerce.features.auth.type.EmailType;
import com.buutruong.ecommerce.features.auth.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final EmailTokenService emailTokenService;
    private final PasswordEncoder passwordEncoder;
    private final AuthUtil authUtil;

    @Override
    @Transactional
    public void verifyEmail(String token) {
        EmailToken emailConfirmToken = emailTokenService.findByToken(token);

        if (!authUtil.isValidToken(emailConfirmToken, EmailType.VERIFY_EMAIL)) {
            throw new IllegalArgumentException("Invalid token");
        }

        String email = emailConfirmToken.getEmail();
        userRepository.enableUser(email, true);
        emailTokenService.deleteToken(token);
    }

    @Override
    public void sendResetPasswordEmail(String email) {
        authUtil.sendVerifyEmail(EmailType.RESET_PASSWORD, email, null);
    }

    @Override
    public void sendDeletionVerificationEmail(String email) {
        authUtil.sendVerifyEmail(EmailType.DELETE_ACCOUNT, email, null);
    }

    @Override
    public void sendUpdateVerificationEmail(String newEmail, @NotNull String oldEmail) {
        if (oldEmail.equals(newEmail)) {
            throw new IllegalArgumentException("New email is the same as the old email");
        }

        authUtil.sendVerifyEmail(EmailType.UPDATE_EMAIL_ADDRESS, newEmail, oldEmail);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Transactional
    @Override
    public void changePassword(String token, ChangePasswordRequest request) {
        EmailToken emailConfirmToken = emailTokenService.findByToken(token);

        if (!authUtil.isValidToken(emailConfirmToken, EmailType.RESET_PASSWORD)) {
            throw new IllegalArgumentException("Invalid token");
        }

        User user = userRepository.findByEmail(emailConfirmToken.getEmail()).orElse(null);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        if (request.getConfirmed() == null || !request.getConfirmed().equals(request.getPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("New passwords should be different");
        }

        userRepository.updatePassword(user.getEmail(), passwordEncoder.encode(request.getPassword()));
        emailTokenService.deleteToken(token);
    }

    @Override
    @Transactional
    public int updateEmailAddress(String token, String oldEmail, CustomUserPrincipal principal) {
        EmailToken emailConfirmToken = emailTokenService.findByToken(token);

        if (!authUtil.isValidToken(emailConfirmToken, EmailType.UPDATE_EMAIL_ADDRESS)) {
            throw new IllegalArgumentException("Invalid token");
        }

        int rowAffected = userRepository.updateEmail(oldEmail, emailConfirmToken.getEmail());

        if (rowAffected > 0) {
            authUtil.sendNotificationEmail(EmailType.UPDATE_EMAIL_ADDRESS, oldEmail);
            emailTokenService.deleteToken(token);

            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                    emailConfirmToken.getEmail(),
                    principal.getUser().getPassword()
            ));
        }

        return rowAffected;
    }

    @Override
    public int disabledUser(String email) {
        return userRepository.disabledUser(email);
    }

    @Transactional
    @Override
    public void deleteAccount(String token) {
        EmailToken emailConfirmToken = emailTokenService.findByToken(token);

        if (!authUtil.isValidToken(emailConfirmToken, EmailType.DELETE_ACCOUNT)) {
            throw new IllegalArgumentException("Invalid token");
        }

        userRepository.deleteByEmail(emailConfirmToken.getEmail());
        emailTokenService.deleteToken(token);
        SecurityContextHolder.getContext().setAuthentication(null);
    }
}
