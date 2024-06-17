package com.buutruong.ecommerce.features.product.service;

import com.buutruong.ecommerce.features.product.model.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();
    Category getCategoryByName(String categoryName);
    Category saveCategory(Category category);
    void deleteCategoryByName(String categoryName);
}
