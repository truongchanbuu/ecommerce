package com.buutruong.ecommerce.features.auth.service.impl;

import com.buutruong.ecommerce.features.auth.model.EmailToken;
import com.buutruong.ecommerce.features.auth.repository.EmailTokenRepository;
import com.buutruong.ecommerce.features.auth.service.EmailTokenService;
import com.buutruong.ecommerce.features.auth.type.EmailType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EmailTokenServiceImpl implements EmailTokenService {
    private final EmailTokenRepository emailTokenRepository;

    @Override
    public void saveVerifyToken(EmailToken emailToken) throws IllegalArgumentException {
        emailTokenRepository.save(emailToken);
    }

    @Override
    public EmailToken getValidToken(String email, EmailType type) {
        return emailTokenRepository.findByEmailAndTypeAndExpiredAtAfter(email, type, LocalDateTime.now()).orElse(null);
    }

    @Override
    public EmailToken findByToken(String token) {
        return emailTokenRepository.findByToken(token).orElse(null);
    }

    @Override
    public void deleteToken(String token) {
        emailTokenRepository.deleteByToken(token);
    }
}
