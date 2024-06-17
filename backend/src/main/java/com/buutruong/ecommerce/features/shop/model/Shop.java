package com.buutruong.ecommerce.features.shop.model;

import com.buutruong.ecommerce.features.auth.model.User;
import com.buutruong.ecommerce.features.product.model.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "shops")
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Shop {
    @Id
    private String shopId;
    private String shopName;
    @Column(columnDefinition = "TEXT")
    private String description;
    private String shopAddress;
    private String shopAvatar;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner", unique = true, nullable = false)
    private User owner;

    @OneToMany(mappedBy = "shop", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Product> products;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
