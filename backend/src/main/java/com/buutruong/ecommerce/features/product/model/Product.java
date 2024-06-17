package com.buutruong.ecommerce.features.product.model;

import com.buutruong.ecommerce.features.shop.model.Shop;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Table(name = "products")
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    private String pid;
    private String productName;
    @Column(columnDefinition = "TEXT")
    private String productDescription;
    @Column(precision = 20, scale = 2)
    private BigDecimal productPrice;

    @OneToMany(mappedBy = "product", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<ProductImage> productImages;

    @ManyToOne
    @JoinColumn(referencedColumnName = "category_name")
    private Category category;

    @ManyToOne
    @JoinColumn(referencedColumnName = "shop_name")
    private Shop shop;

    @ManyToOne
    @JoinColumn(referencedColumnName = "brand_name")
    private Brand brand;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
