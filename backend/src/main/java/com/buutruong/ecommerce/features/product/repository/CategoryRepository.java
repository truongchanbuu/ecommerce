package com.buutruong.ecommerce.features.product.repository;

import com.buutruong.ecommerce.features.product.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByCategoryName(String name);
    void deleteByCategoryName(String name);
}
