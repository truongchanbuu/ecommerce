package com.buutruong.ecommerce.features.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class AddProductRequest {
    @NotBlank(message = "Product must have a name")
    private String productName;
    @NotBlank(message = "Please describe the product")
    private String productDescription;
    @NotNull(message = "Price is required")
    @Positive(message = "Price cannot be less than 0")
    private BigDecimal productPrice;
    @NotBlank(message = "Product must have category")
    private String categoryName;
    @NotBlank(message = "Product must have brand")
    private String brandName;
    @NotEmpty(message = "Product should have at least an image")
    private List<String> imageUrls;
    @NotNull(message = "You must have a shop to sell this")
    private String shopId;
}
