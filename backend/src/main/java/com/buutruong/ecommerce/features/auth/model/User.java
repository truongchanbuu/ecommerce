package com.buutruong.ecommerce.features.auth.model;

import com.buutruong.ecommerce.features.auth.type.AuthProvider;
import com.buutruong.ecommerce.features.auth.type.Gender;
import com.buutruong.ecommerce.features.auth.type.Role;
import com.buutruong.ecommerce.features.shop.model.Shop;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Table(name = "users")
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private String email;
    private String username;
    private String password;
    private String phone;
    private String photoUrl;
    private LocalDate birthday;

    @Builder.Default
    private Boolean enabled = false;

    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime modifiedAt;

    @OneToOne(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Shop shop;
}
