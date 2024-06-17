package com.buutruong.ecommerce.features.auth.service;

import com.buutruong.ecommerce.features.auth.dto.LoginRequest;
import com.buutruong.ecommerce.features.auth.dto.RegisterRequest;

public interface AuthService {
    void register(RegisterRequest registerRequest);
    String login(LoginRequest loginRequest);
}
