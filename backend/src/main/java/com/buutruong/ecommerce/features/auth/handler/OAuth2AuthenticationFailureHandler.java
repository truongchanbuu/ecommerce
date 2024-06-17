package com.buutruong.ecommerce.features.auth.handler;

import com.buutruong.ecommerce.core.util.CookieUtils;
import com.buutruong.ecommerce.features.auth.repository.HttpCookieOAuth2AuthorizationRequestRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

import static com.buutruong.ecommerce.features.auth.repository.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_COOKIE_NAME;

@RequiredArgsConstructor
@Component
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        Cookie targetUriCookie = CookieUtils.getCookie(request, REDIRECT_URI_COOKIE_NAME);

        String targetUri = targetUriCookie != null ? targetUriCookie.getValue() : "/";
        targetUri = UriComponentsBuilder.fromUriString(targetUri)
                .queryParam("error", exception.getLocalizedMessage())
                .build().toUriString();


        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUri);
    }
}
