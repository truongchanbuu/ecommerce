package com.buutruong.ecommerce.core.annotation;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Documented
@AuthenticationPrincipal
@Retention(RetentionPolicy.RUNTIME)
public @interface CurrentUser {
}
