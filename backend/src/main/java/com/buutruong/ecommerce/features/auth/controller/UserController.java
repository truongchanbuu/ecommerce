package com.buutruong.ecommerce.features.auth.controller;

import com.buutruong.ecommerce.core.annotation.CurrentUser;
import com.buutruong.ecommerce.core.data.ApiResponse;
import com.buutruong.ecommerce.features.auth.dto.ChangePasswordRequest;
import com.buutruong.ecommerce.features.auth.dto.UpdateEmailRequest;
import com.buutruong.ecommerce.features.auth.model.CustomUserPrincipal;
import com.buutruong.ecommerce.features.auth.model.User;
import com.buutruong.ecommerce.features.auth.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    
    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<User>> getProfile(@CurrentUser CustomUserPrincipal principal) {
        try {
            User user = userService.getUserByEmail(principal.getUser().getEmail());
            return ResponseEntity.ok(ApiResponse.<User>builder()
                    .code(200)
                    .message("Success to get your profile")
                    .data(user)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.<User>builder()
                    .code(400)
                    .message(e.getMessage())
                    .build());
        }
    }

    @GetMapping("/verify-email")
    public ResponseEntity<ApiResponse<Void>> verifyEmail(@RequestParam String token) {
        try {
            userService.verifyEmail(token);
            return ResponseEntity.ok(ApiResponse.<Void>builder()
                    .code(200)
                    .message("Email verified")
                    .build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.<Void>builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message("Failed to verify your email: " + e.getMessage())
                    .build());
        }
    }

    @GetMapping("/forgot-password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Void>> forgotPassword(@CurrentUser CustomUserPrincipal principal) {
        userService.sendResetPasswordEmail(principal.getUser().getEmail());

        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .code(200)
                .message("A reset password mail has been sent to your email.")
                .build());
    }

    @PatchMapping("/reset-password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Void>> resetPassword(
            @RequestParam String token,
            @RequestBody @Valid ChangePasswordRequest request) {
        try {
            userService.changePassword(token, request);

            return ResponseEntity.ok(ApiResponse.<Void>builder()
                    .code(200)
                    .message("Password changed")
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(400).body(ApiResponse.<Void>builder()
                    .code(400)
                    .message(e.getMessage())
                    .build());
        }
    }

    @GetMapping("/delete/verification")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Void>> deleteAccount(@CurrentUser CustomUserPrincipal principal) {
        userService.sendDeletionVerificationEmail(principal.getUser().getEmail());

        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .code(200)
                .message("A verification mail has been sent to your email.")
                .build());
    }

    @DeleteMapping("/delete")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Void>> delete(@RequestParam("token") String token) {
        try {
            userService.deleteAccount(token);
            return ResponseEntity.ok().body(ApiResponse.<Void>builder()
                    .code(200)
                    .message("Deleted your account")
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.<Void>builder()
                    .code(400)
                    .message(e.getMessage())
                    .build());
        }
    }

    @PostMapping("/update-email/verification")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Void>> updateEmailVerification(
            @CurrentUser CustomUserPrincipal principal,
            @RequestBody @Valid UpdateEmailRequest request) {
        try {
            userService.sendUpdateVerificationEmail(request.getNewEmail(), principal.getUser().getEmail());
            return ResponseEntity.ok(ApiResponse.<Void>builder()
                    .code(200)
                    .message("A verification email is sent to your new email")
                    .build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.ok(ApiResponse.<Void>builder()
                    .code(400)
                    .message(e.getMessage())
                    .build());
        }
    }

    @PatchMapping("/update-email")
    public ResponseEntity<ApiResponse<Void>> updateEmail(
            @RequestParam("token") String token,
            @RequestParam("oldEmail") String oldEmail,
            @CurrentUser CustomUserPrincipal principal) {
        int rowAffected = userService.updateEmailAddress(token, oldEmail, principal);

        if (rowAffected == 0) {
            return ResponseEntity.badRequest().body(ApiResponse.<Void>builder()
                    .code(400)
                    .message("Email does not exist")
                    .build());
        }

        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .code(200)
                .message("Your email has been updated")
                .build());
    }

    @PostMapping("/{email}/block-request")
    public ResponseEntity<ApiResponse<Void>> blockRequest(@PathVariable("email") String email) {
        int rowAffected = userService.disabledUser(email);

        if (rowAffected == 0) {
            return ResponseEntity.badRequest().body(ApiResponse.<Void>builder()
                    .code(400)
                    .message("Your account does not exist")
                    .build());
        }

        return ResponseEntity.badRequest().body(ApiResponse.<Void>builder()
                .code(200)
                .message("Your account has been disabled")
                .build());
    }
}
