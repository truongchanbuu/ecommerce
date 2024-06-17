package com.buutruong.ecommerce.features.product.repository;

import com.buutruong.ecommerce.features.product.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
    Optional<Brand> findByBrandName(String name);
    void deleteByBrandName(String name);
}
