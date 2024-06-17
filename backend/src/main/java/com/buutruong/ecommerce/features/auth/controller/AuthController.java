package com.buutruong.ecommerce.features.auth.controller;

import com.buutruong.ecommerce.core.data.ApiResponse;
import com.buutruong.ecommerce.features.auth.dto.LoginRequest;
import com.buutruong.ecommerce.features.auth.dto.RegisterRequest;
import com.buutruong.ecommerce.features.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(@RequestBody @Valid RegisterRequest request) {
        try {
            authService.register(request);

            URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/profile").buildAndExpand(request.getEmail()).toUri();

            return ResponseEntity.created(location).body(ApiResponse.<String>builder()
                    .code(HttpStatus.OK.value())
                    .message("An verification mail has been sent to your email.")
                    .build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.<String>builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message(e.getMessage())
                    .build()
            );
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody @Valid LoginRequest loginRequest) {
        try {
            String jwt = authService.login(loginRequest);

            return ResponseEntity.ok(ApiResponse.<String>builder()
                    .code(HttpStatus.ACCEPTED.value())
                    .message("Login successfully")
                    .data(jwt)
                    .build());
        } catch (Exception e) {
            int code = HttpStatus.BAD_REQUEST.value();

            if (e instanceof DisabledException || e instanceof LockedException) {
                code = HttpStatus.FORBIDDEN.value();
            }

            return ResponseEntity.status(code).body(ApiResponse.<String>builder()
                    .code(code)
                    .message(e.getMessage())
                    .build());
        }
    }
}
