package com.buutruong.ecommerce.features.product.repository;

import com.buutruong.ecommerce.features.product.model.Category;
import com.buutruong.ecommerce.features.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    Optional<List<Product>> findAllByCategory_CategoryName(String categoryName);
    Optional<List<Product>> findAllByBrand_BrandName(String brandName);
}
