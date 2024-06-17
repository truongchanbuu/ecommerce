package com.buutruong.ecommerce.features.auth.service.impl;

import com.buutruong.ecommerce.features.auth.dto.ChangePasswordRequest;
import com.buutruong.ecommerce.features.auth.dto.LoginRequest;
import com.buutruong.ecommerce.features.auth.dto.RegisterRequest;
import com.buutruong.ecommerce.features.auth.model.CustomUserPrincipal;
import com.buutruong.ecommerce.features.auth.model.EmailToken;
import com.buutruong.ecommerce.features.auth.model.User;
import com.buutruong.ecommerce.features.auth.repository.UserRepository;
import com.buutruong.ecommerce.features.auth.service.AuthService;
import com.buutruong.ecommerce.features.auth.service.EmailTokenService;
import com.buutruong.ecommerce.features.auth.service.JwtService;
import com.buutruong.ecommerce.features.auth.type.EmailType;
import com.buutruong.ecommerce.features.auth.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthUtil authUtil;
    private final AuthenticationManager authenticationManager;

    @Override
    public void register(@NotNull RegisterRequest registerRequest) throws IllegalArgumentException {
        boolean isEmailExisted = userRepository.findByEmail(registerRequest.getEmail()).isPresent();

        if (isEmailExisted) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = authUtil.createUser(registerRequest);

        userRepository.save(user);
        authUtil.sendVerifyEmail(EmailType.VERIFY_EMAIL, user.getEmail(), null);
    }

    @Override
    public String login(@NotNull LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            return jwtService.generateToken((CustomUserPrincipal) authentication.getPrincipal());
        } catch (DisabledException e) {
            throw new DisabledException("User is disabled");
        } catch (LockedException e) {
            throw new LockedException("User is locked");
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid email or password");
        }
    }
}
