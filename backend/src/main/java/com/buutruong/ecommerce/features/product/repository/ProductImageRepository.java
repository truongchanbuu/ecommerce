package com.buutruong.ecommerce.features.product.repository;

import com.buutruong.ecommerce.features.product.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
}
