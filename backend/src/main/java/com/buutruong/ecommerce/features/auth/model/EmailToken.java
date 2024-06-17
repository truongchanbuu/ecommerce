package com.buutruong.ecommerce.features.auth.model;

import com.buutruong.ecommerce.features.auth.type.EmailType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "verification_tokens")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    private String email;
    private String token;

    @Enumerated(EnumType.STRING)
    private EmailType type;

    @CreationTimestamp
    private LocalDateTime createdAt;
    private LocalDateTime expiredAt;
}
