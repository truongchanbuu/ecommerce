package com.buutruong.ecommerce.config;

import com.buutruong.ecommerce.features.auth.controller.RestAuthenticationEntryPoint;
import com.buutruong.ecommerce.features.auth.filter.JwtAuthenticationFilter;
import com.buutruong.ecommerce.features.auth.handler.OAuth2AuthenticationFailureHandler;
import com.buutruong.ecommerce.features.auth.handler.OAuth2AuthenticationSuccessHandler;
import com.buutruong.ecommerce.features.auth.service.CustomOAuth2Service;
import com.buutruong.ecommerce.features.auth.type.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;
    private final CustomOAuth2Service customOAuth2Service;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(requests -> requests.requestMatchers("/api/v*/checkout", "/api/v*/cart").authenticated())
                .authorizeHttpRequests(requests -> requests.requestMatchers("/api/v*/admin").hasRole(Role.ADMIN.name()))
                .authorizeHttpRequests(requests -> requests.anyRequest().permitAll())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .exceptionHandling(exception -> exception.authenticationEntryPoint(new RestAuthenticationEntryPoint()))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .oauth2Login(oauth2 -> oauth2.userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2Service))
                        .successHandler(oAuth2AuthenticationSuccessHandler)
                        .failureHandler(oAuth2AuthenticationFailureHandler));

        return http.build();
    }
}
