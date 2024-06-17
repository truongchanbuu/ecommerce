package com.buutruong.ecommerce.features.auth.service;

import com.buutruong.ecommerce.features.auth.dto.ChangePasswordRequest;
import com.buutruong.ecommerce.features.auth.model.CustomUserPrincipal;
import com.buutruong.ecommerce.features.auth.model.User;

public interface UserService {
    void verifyEmail(String token);
    void changePassword(String token, ChangePasswordRequest changePasswordRequest);
    void sendResetPasswordEmail(String email);
    User getUserByEmail(String email);
    void sendDeletionVerificationEmail(String email);
    void deleteAccount(String token);
    void sendUpdateVerificationEmail(String email, String oldEmail);
    int updateEmailAddress(String token, String oldEmail, CustomUserPrincipal principal);
    int disabledUser(String email);
}
