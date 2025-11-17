package com.example.simpleauth.util;

import com.example.simpleauth.entity.User;
import com.example.simpleauth.service.UserService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtils {
    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationMs:3600000}")
    private int jwtExpirationMs;

    private final UserService userService;

    public JwtUtils(UserService userService) {
        this.userService = userService;
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public String generateJwtToken(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        User userPrincipal;
        if (principal instanceof OAuth2User) {
            userPrincipal = userService.saveOrUpdateFromOAuth((OAuth2User) principal);
        } else {
            userPrincipal = (User) principal;
        }
        return Jwts.builder()
                .subject(userPrincipal.getEmail())
                .claim("roles", userPrincipal.getRoles())
                .claim("fullName", userPrincipal.getFullName())
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(authToken);
            return true;
        } catch (Exception e) {
            // Log
        }
        return false;
    }
}