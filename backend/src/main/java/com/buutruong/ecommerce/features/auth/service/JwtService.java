package com.buutruong.ecommerce.features.auth.service;

import com.buutruong.ecommerce.features.auth.model.CustomUserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${app.auth.token-secret}")
    private String SECRET;

    @Value("${app.auth.token-expire-in}")
    private long EXPIRE_IN;

    private String doGenerateToken(Map<String, Object> claims, CustomUserPrincipal userPrincipal) {
        return Jwts.builder()
                .claims(claims)
                .subject(userPrincipal.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRE_IN))
                .signWith(getSecretKey())
                .compact();
    }

    public String generateToken(CustomUserPrincipal userPrincipal) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userPrincipal);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(token).getPayload();
    }

    private <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String extractEmail(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }

    private SecretKey getSecretKey() {
        byte[] bytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(bytes);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        return !isTokenExpired(token) && userDetails.getUsername().equals(extractEmail(token));
    }
}
