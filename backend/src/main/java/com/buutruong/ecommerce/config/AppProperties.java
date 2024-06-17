package com.buutruong.ecommerce.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "app")
@Data
public class AppProperties {
    private final Auth auth = new Auth();
    private final OAuth2 oauth2 = new OAuth2();

    @Data
    public static class Auth {
        private String tokenSecret;
        private Long tokenExpireIn;
    }

    @Data
    public static class OAuth2 {
        private List<String> authorizedRedirectUris = new ArrayList<>();
    }
}
