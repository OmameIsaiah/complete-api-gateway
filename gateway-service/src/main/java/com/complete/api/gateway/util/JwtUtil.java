package com.complete.api.gateway.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public Mono<Claims> extractAllClaims(String token) {
        return Mono.fromCallable(() -> {
            try {
                return Jwts.parserBuilder()
                        .setSigningKey(getSigningKey())
                        .build()
                        .parseClaimsJws(token)
                        .getBody();
            } catch (JwtException | IllegalArgumentException e) {
                throw new RuntimeException("Invalid JWT token", e);
            }
        });
    }

    public Mono<String> extractUserId(String token) {
        return extractAllClaims(token)
                .map(Claims::getSubject);
    }

    public Mono<Boolean> validateToken(String token) {
        return Mono.fromCallable(() -> {
            try {
                Jwts.parserBuilder()
                        .setSigningKey(getSigningKey())
                        .build()
                        .parseClaimsJws(token);
                return true;
            } catch (JwtException | IllegalArgumentException e) {
                return false;
            }
        });
    }

    public Mono<Boolean> isTokenExpired(String token) {
        return extractAllClaims(token)
                .map(claims -> claims.getExpiration().before(new Date()))
                .onErrorResume(ExpiredJwtException.class, e -> Mono.just(true))
                .onErrorResume(Exception.class, e -> Mono.just(true));
    }
}