package com.buutruong.ecommerce.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class CorsConfig implements WebMvcConfigurer {
    @Value("${app.cors.allowedOrigins}")
    private String[] allowedOrigins;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        long MAX_AGE_SEC = 3600L;
        registry.addMapping("/**")
                .allowedOrigins(allowedOrigins)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATH", "OPTIONS")
                .allowCredentials(true)
                .maxAge(MAX_AGE_SEC);
    }
}
