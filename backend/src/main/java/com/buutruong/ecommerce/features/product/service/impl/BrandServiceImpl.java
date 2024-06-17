package com.buutruong.ecommerce.features.product.service.impl;

import com.buutruong.ecommerce.features.product.model.Brand;
import com.buutruong.ecommerce.features.product.repository.BrandRepository;
import com.buutruong.ecommerce.features.product.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;

    @Override
    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }

    @Override
    public Brand saveBrand(Brand brand) {
        return brandRepository.save(brand);
    }

    @Override
    public void deleteBrandByName(String brandName) {
        brandRepository.deleteByBrandName(brandName);
    }
}
