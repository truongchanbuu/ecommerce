package com.buutruong.ecommerce.core.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.SerializationUtils;

import java.util.Base64;

public class CookieUtils {
    public static Cookie getCookie(HttpServletRequest request, String key) {
        Cookie[] cookies = request.getCookies();

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(key)) {
                return cookie;
            }
        }

        return null;
    }

    public static void addCookie(HttpServletResponse response, String key, String value, int maxAge, String domain) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(maxAge);
        cookie.setDomain(domain);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);

        response.addCookie(cookie);
    }

    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String key) {
        Cookie[] cookies = request.getCookies();

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(key)) {
                cookie.setMaxAge(0);
                cookie.setPath("/");
                cookie.setValue("");
                response.addCookie(cookie);
            }
        }
    }

    public static String serialize(Object object) {
        return Base64.getUrlEncoder().encodeToString(SerializationUtils.serialize(object));
    }

    public static <T> T deserialize(Cookie cookie, Class<T> clazz) {
        if (cookie == null) return null;
        String value = cookie.getValue();

        return clazz.cast(SerializationUtils.deserialize(Base64.getUrlDecoder().decode(value)));
    }
}
