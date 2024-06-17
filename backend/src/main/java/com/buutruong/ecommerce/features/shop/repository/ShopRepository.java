package com.buutruong.ecommerce.features.shop.repository;

import com.buutruong.ecommerce.features.shop.model.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopRepository extends JpaRepository<Shop, String> {}
