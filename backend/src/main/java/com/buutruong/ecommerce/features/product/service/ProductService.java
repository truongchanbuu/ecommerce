package com.buutruong.ecommerce.features.product.service;

import com.buutruong.ecommerce.features.product.dto.AddProductRequest;
import com.buutruong.ecommerce.features.product.model.Product;

import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();
    Product getProductById(String pid);
    List<Product> getProductsByCategory(String category);
    List<Product> getProductsByBrand(String brand);
    Product addProduct(AddProductRequest request);
    void deleteProduct(String pid);
}
