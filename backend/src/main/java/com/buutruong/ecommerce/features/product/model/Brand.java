package com.buutruong.ecommerce.features.product.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Table(name = "brands")
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String brandName;
    private String brandDescription;
    private String brandLogo;

    @OneToMany(mappedBy = "brand", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Product> products;
}
