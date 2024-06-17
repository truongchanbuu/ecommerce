package com.buutruong.ecommerce.features.product.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Table(name = "categories")
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;
    @Column(nullable = false, unique = true)
    private String categoryName;
    @OneToMany(mappedBy = "category")
    private List<Product> products;
}
