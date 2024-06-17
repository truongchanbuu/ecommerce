package com.buutruong.ecommerce.features.product.controller;

import com.buutruong.ecommerce.core.data.ApiResponse;
import com.buutruong.ecommerce.features.product.dto.AddProductRequest;
import com.buutruong.ecommerce.features.product.model.Product;
import com.buutruong.ecommerce.features.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/")
    public ResponseEntity<ApiResponse<List<Product>>> getProduct() {
        return ResponseEntity.ok(ApiResponse.<List<Product>>builder()
                .code(200)
                .message("Get all products")
                .data(productService.getAllProducts())
                .build());
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<Product>> addProduct(@RequestBody AddProductRequest request) {
        Product product = productService.addProduct(request);

        return ResponseEntity.ok(ApiResponse.<Product>builder()
                .code(200)
                .message("Product added")
                .data(product)
                .build());
    }
}
