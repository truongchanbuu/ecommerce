package com.buutruong.ecommerce.features.auth.model;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class CustomUserPrincipal implements UserDetails, OAuth2User, OidcUser {
    private User user;
    private Map<String, Object> attributes;
    private Map<String, Object> claims;
    private OidcIdToken oidcToken;

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(user.getRole().name()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.getEnabled();
    }

    @Override
    public boolean isEnabled() {
        return user.getEnabled();
    }

    @Override
    public String getName() {
        return user.getUsername();
    }

    @Override
    public Map<String, Object> getClaims() {
        return claims;
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return OidcUserInfo.builder()
                .email(user.getEmail())
                .name(user.getUsername())
                .gender(user.getGender().name())
                .birthdate(user.getBirthday().toString())
                .emailVerified(user.getEnabled())
                .phoneNumber(user.getPhone())
                .build();
    }

    @Override
    public OidcIdToken getIdToken() {
        return oidcToken;
    }
}
