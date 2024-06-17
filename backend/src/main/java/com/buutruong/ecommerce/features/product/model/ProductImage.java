package com.buutruong.ecommerce.features.product.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "product_images")
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_image_id")
    private Long productImageId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id", referencedColumnName = "pid")
    private Product product;

    @Column(name = "image_url")
    private String imageUrl;
}
