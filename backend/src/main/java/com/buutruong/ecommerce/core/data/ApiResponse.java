package com.buutruong.ecommerce.core.data;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ApiResponse<T> {
    int code;
    T data;
    String message;
}
