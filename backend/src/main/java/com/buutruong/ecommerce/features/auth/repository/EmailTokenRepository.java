package com.buutruong.ecommerce.features.auth.repository;

import com.buutruong.ecommerce.features.auth.model.EmailToken;
import com.buutruong.ecommerce.features.auth.type.EmailType;
import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface EmailTokenRepository extends JpaRepository<EmailToken, Long> {
    Optional<EmailToken> findByToken(String token);
    Optional<EmailToken> findByEmailAndTypeAndExpiredAtAfter(@Email String email, EmailType type, LocalDateTime expiredAt);
    void deleteByToken(String token);
}
