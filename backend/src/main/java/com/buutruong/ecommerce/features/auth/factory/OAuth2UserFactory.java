package com.buutruong.ecommerce.features.auth.factory;

import com.buutruong.ecommerce.features.auth.model.FacebookOAuth2UserInfo;
import com.buutruong.ecommerce.features.auth.model.GithubOAuth2UserInfo;
import com.buutruong.ecommerce.features.auth.model.GoogleOAuth2UserInfo;
import com.buutruong.ecommerce.features.auth.model.OAuth2UserInfo;

import java.util.Map;

public class OAuth2UserFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        return switch (registrationId.trim().toLowerCase()) {
            case "google" -> new GoogleOAuth2UserInfo(attributes);
            case "facebook" -> new FacebookOAuth2UserInfo(attributes);
            case "github" -> new GithubOAuth2UserInfo(attributes);
            default -> throw new IllegalArgumentException("Unsupported login method: " + registrationId);
        };
    }
}
