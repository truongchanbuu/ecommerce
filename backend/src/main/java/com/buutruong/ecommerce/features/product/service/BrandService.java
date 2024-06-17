package com.buutruong.ecommerce.features.product.service;

import com.buutruong.ecommerce.features.product.model.Brand;

import java.util.List;

public interface BrandService {
    List<Brand> getAllBrands();
    Brand saveBrand(Brand brand);
    void deleteBrandByName(String brandName);
}
