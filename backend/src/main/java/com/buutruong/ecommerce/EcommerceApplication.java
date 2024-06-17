package com.buutruong.ecommerce;

import com.buutruong.ecommerce.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class EcommerceApplication {
	public static void main(String[] args) {
		SpringApplication.run(EcommerceApplication.class, args);
	}
}
