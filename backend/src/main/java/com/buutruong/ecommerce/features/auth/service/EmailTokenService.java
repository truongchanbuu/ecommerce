package com.buutruong.ecommerce.features.auth.service;

import com.buutruong.ecommerce.features.auth.model.EmailToken;
import com.buutruong.ecommerce.features.auth.type.EmailType;

public interface EmailTokenService {
    void saveVerifyToken(EmailToken emailToken);
    EmailToken getValidToken(String email, EmailType type);
    EmailToken findByToken(String token);
    void deleteToken(String token);
}
