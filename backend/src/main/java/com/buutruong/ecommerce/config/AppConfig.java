package com.buutruong.ecommerce.config;

import com.buutruong.ecommerce.features.auth.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class AppConfig {
    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        daoAuthenticationProvider.setUserDetailsService(customUserDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());

        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
