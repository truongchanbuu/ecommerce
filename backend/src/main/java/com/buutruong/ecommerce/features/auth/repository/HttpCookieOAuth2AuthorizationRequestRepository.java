package com.buutruong.ecommerce.features.auth.repository;

import com.buutruong.ecommerce.core.util.CookieUtils;
import com.nimbusds.oauth2.sdk.util.StringUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Repository;

@Repository
public class HttpCookieOAuth2AuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {
    public static final String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_authorization_request";
    public static final String REDIRECT_URI_COOKIE_NAME = "redirect_uri";
    private static final int cookieMaxAge = 180;

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        Cookie cookie = CookieUtils.getCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
        OAuth2AuthorizationRequest authorizationRequest = null;

        if (cookie != null) {
            authorizationRequest = CookieUtils.deserialize(cookie, OAuth2AuthorizationRequest.class);
        }

        return authorizationRequest;
    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        if (authorizationRequest == null) {
            CookieUtils.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
            CookieUtils.deleteCookie(request, response, REDIRECT_URI_COOKIE_NAME);
            return;
        }

        CookieUtils
                .addCookie(response,
                        OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME,
                        CookieUtils.serialize(authorizationRequest),
                        cookieMaxAge,
                        "");

        String redirectUriAfterLogin = request.getParameter(REDIRECT_URI_COOKIE_NAME);

        if (StringUtils.isNotBlank(redirectUriAfterLogin)) {
            CookieUtils.addCookie(response,
                    REDIRECT_URI_COOKIE_NAME,
                    redirectUriAfterLogin,
                    cookieMaxAge, "");
        }
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse response) {
        return this.loadAuthorizationRequest(request);
    }

    public void removeAuthorizationRequestCookies(HttpServletRequest request, HttpServletResponse response) {
        CookieUtils.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
        CookieUtils.deleteCookie(request, response, REDIRECT_URI_COOKIE_NAME);
    }
}
