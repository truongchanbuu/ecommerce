package com.buutruong.ecommerce.features.product.service.impl;

import com.buutruong.ecommerce.features.product.model.Category;
import com.buutruong.ecommerce.features.product.repository.CategoryRepository;
import com.buutruong.ecommerce.features.product.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategoryByName(String categoryName) {
        return categoryRepository.findByCategoryName(categoryName).orElse(null);
    }

    @Override
    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public void deleteCategoryByName(String categoryName) {
        categoryRepository.deleteByCategoryName(categoryName);
    }
}
