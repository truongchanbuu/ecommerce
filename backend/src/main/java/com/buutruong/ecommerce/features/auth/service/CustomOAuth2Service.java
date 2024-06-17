package com.buutruong.ecommerce.features.auth.service;

import com.buutruong.ecommerce.features.auth.factory.OAuth2UserFactory;
import com.buutruong.ecommerce.features.auth.model.CustomUserPrincipal;
import com.buutruong.ecommerce.features.auth.model.OAuth2UserInfo;
import com.buutruong.ecommerce.features.auth.model.User;
import com.buutruong.ecommerce.features.auth.repository.UserRepository;
import com.buutruong.ecommerce.features.auth.type.AuthProvider;
import com.buutruong.ecommerce.features.auth.type.Role;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2Service extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        try {
            return processOAuth2User(userRequest, oAuth2User);
        } catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(@NotNull OAuth2UserRequest userRequest, @NotNull OAuth2User oAuth2User) {
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2UserInfo userInfo = OAuth2UserFactory.getOAuth2UserInfo(registrationId, oAuth2User.getAttributes());

        if (StringUtils.isEmpty(userInfo.getEmail())) {
            throw new OAuth2AuthenticationException("Email not found");
        }

        Optional<User> userOptional = userRepository.findByEmail(userInfo.getEmail());
        User user;
        if (userOptional.isPresent()) {
            user = userOptional.get();
            if (!user.getProvider().equals(AuthProvider.valueOf(registrationId))) {
                throw new OAuth2AuthenticationException("Looks like you're signed up with " +
                        user.getProvider() + " account. Please use your " +
                        user.getProvider() + " account to login.");
            }
            user = updateExistingUser(user, userInfo);
        } else {
            user = registerUser(userRequest, userInfo);
        }

        return CustomUserPrincipal.builder()
                .user(user)
                .attributes(oAuth2User.getAttributes())
                .build();
    }

    private User registerUser(OAuth2UserRequest userRequest, OAuth2UserInfo userInfo) {
        User user = User.builder()
                .email(userInfo.getEmail())
                .username(userInfo.getUsername())
                .photoUrl(userInfo.getPhotoUrl())
                .provider(AuthProvider.valueOf(userRequest.getClientRegistration().getRegistrationId()))
                .role(Role.CUSTOMER)
                .build();

        return userRepository.save(user);
    }

    private User updateExistingUser(User user, OAuth2UserInfo userInfo) {
        user.setUsername(userInfo.getUsername());
        user.setPhotoUrl(userInfo.getPhotoUrl());

        return userRepository.save(user);
    }
}
