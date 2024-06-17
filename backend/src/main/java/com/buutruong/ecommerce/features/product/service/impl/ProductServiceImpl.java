package com.buutruong.ecommerce.features.product.service.impl;

import com.buutruong.ecommerce.core.util.StringUtils;
import com.buutruong.ecommerce.features.product.dto.AddProductRequest;
import com.buutruong.ecommerce.features.product.model.Brand;
import com.buutruong.ecommerce.features.product.model.Category;
import com.buutruong.ecommerce.features.product.model.Product;
import com.buutruong.ecommerce.features.product.model.ProductImage;
import com.buutruong.ecommerce.features.product.repository.BrandRepository;
import com.buutruong.ecommerce.features.product.repository.CategoryRepository;
import com.buutruong.ecommerce.features.product.repository.ProductImageRepository;
import com.buutruong.ecommerce.features.product.repository.ProductRepository;
import com.buutruong.ecommerce.features.product.service.ProductService;
import com.buutruong.ecommerce.features.shop.model.Shop;
import com.buutruong.ecommerce.features.shop.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final ShopRepository shopRepository;
    private final ProductImageRepository productImageRepository;

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(String pid) {
        return productRepository.findById(pid).orElse(null);
    }

    @Override
    public List<Product> getProductsByCategory(String categoryName) {
        return productRepository.findAllByCategory_CategoryName(categoryName).orElse(null);
    }

    @Override
    public List<Product> getProductsByBrand(String brandName) {
        return productRepository.findAllByBrand_BrandName(brandName).orElse(null);
    }

    @Override
    @Transactional
    public Product addProduct(@NotNull AddProductRequest request) {
        Product product = createProductWithRequest(request);

        List<ProductImage> images = new ArrayList<>();
        request.getImageUrls().forEach(url -> images.add(ProductImage.builder()
                .imageUrl(url)
                .product(product)
                .build()));

        product.setProductImages(images);

        return productRepository.save(product);
    }

    private Product createProductWithRequest(AddProductRequest request) {
        Category category = categoryRepository.findByCategoryName(request.getCategoryName())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        Brand brand = brandRepository.findByBrandName(request.getBrandName())
                .orElseThrow(() -> new IllegalArgumentException("Brand not found"));
        Shop shop = shopRepository.findById(request.getShopId())
                .orElseThrow(() -> new IllegalArgumentException("Shop not found"));

        return Product.builder()
                .pid(generatePid(request.getCategoryName(), request.getBrandName()))
                .productName(request.getProductName())
                .productPrice(request.getProductPrice())
                .productDescription(request.getProductDescription())
                .category(category)
                .brand(brand)
                .shop(shop)
                .build();
    }

    @Override
    public void deleteProduct(String pid) {
        productRepository.deleteById(pid);
    }

    private String generatePid(String category, String brand) {
        return String.format("%s-%s-%s",
                StringUtils.removeAccentsAndWhiteSpace(category),
                StringUtils.removeAccentsAndWhiteSpace(brand),
                UUID.randomUUID().toString().toUpperCase());
    }
}
