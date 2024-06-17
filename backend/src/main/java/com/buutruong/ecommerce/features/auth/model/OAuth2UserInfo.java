package com.buutruong.ecommerce.features.auth.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public abstract class OAuth2UserInfo {
    protected Map<String, Object> attributes;

    public abstract String getEmail();
    public abstract String getUsername();
    public abstract String getPhotoUrl();
}
