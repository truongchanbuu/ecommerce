package com.buutruong.ecommerce.features.auth.handler;

import com.buutruong.ecommerce.config.AppProperties;
import com.buutruong.ecommerce.core.util.CookieUtils;
import com.buutruong.ecommerce.features.auth.repository.HttpCookieOAuth2AuthorizationRequestRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;

import static com.buutruong.ecommerce.features.auth.repository.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_COOKIE_NAME;

@RequiredArgsConstructor
@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final Logger logger = LoggerFactory.getLogger(OAuth2AuthenticationSuccessHandler.class);
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    private final AppProperties appProperties;

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Cookie redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_COOKIE_NAME);

        if (redirectUri != null && !isAuthorizedRedirectUri(redirectUri.getValue())) {
            throw new IllegalArgumentException("Unauthorized redirect URI: " + redirectUri);
        }

        String targetUri = redirectUri == null ? getDefaultTargetUrl() : redirectUri.getValue();

        return UriComponentsBuilder.fromUriString(targetUri)
                .build().toUriString();
    }

    private boolean isAuthorizedRedirectUri(String uri) {
        URI redirectUri = URI.create(uri);

        return appProperties.getOauth2().getAuthorizedRedirectUris()
                .stream()
                .anyMatch(authorizedUri -> {
                    URI targetUri = URI.create(authorizedUri);
                    return redirectUri.getHost().equalsIgnoreCase(targetUri.getHost()) && redirectUri.getPort() == targetUri.getPort();
                });
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String targetUrl = determineTargetUrl(request, response, authentication);

        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Cannot redirect to target url: {}", targetUrl);
            return;
        }


        logger.info("TARGET URL: {}", targetUrl);
        this.clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
