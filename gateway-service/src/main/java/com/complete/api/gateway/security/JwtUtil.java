package com.complete.api.gateway.security;

import com.nimbusds.jose.util.Base64;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;
    public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS512;
    public static final String AUTHORITIES_KEY = "auth";

    private SecretKey getSecretKey() {
        byte[] keyBytes = Base64.from(secret).decode();
        return new SecretKeySpec(keyBytes, 0, keyBytes.length, JWT_ALGORITHM.getName());
    }

    public Mono<Claims> extractAllClaims(String token) {
        return Mono.fromCallable(() -> {
            try {
                return Jwts.parser()
                        .setSigningKey(secret)
                        .parseClaimsJws(token)
                        .getBody();

                /*return Jwts.parser()
                        .setSigningKey(secret.getBytes())
                        .parseClaimsJws(token)
                        .getBody();*/
                /*return Jwts.parserBuilder()
                        .setSigningKey(getSecretKey())
                        .build()
                        .parseClaimsJws(token)
                        .getBody();*/
            } catch (ExpiredJwtException e) {
                throw new RuntimeException("JWT token expired", e);
            } catch (MalformedJwtException e) {
                throw new RuntimeException("Invalid JWT token format", e);
            } catch (SignatureException e) {
                throw new RuntimeException("JWT signature validation failed", e);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("JWT token is empty or null", e);
            } catch (JwtException e) {
                throw new RuntimeException("Invalid JWT token", e);
            }
        });
    }

    public Mono<String> extractUserId(String token) {
        return extractAllClaims(token)
                .map(claims -> {
                    // Try to get user ID from different possible claims
                    Object userId = claims.get("user-id");
                    if (userId != null) {
                        return userId.toString();
                    }

                    // Fall back to subject if user-id claim is not present
                    return claims.getSubject();
                })
                .onErrorResume(e -> {
                    // Log the error for debugging
                    System.err.println("Error extracting user ID: " + e.getMessage());
                    return Mono.error(e);
                });
    }

    public Mono<Boolean> validateToken(String token) {
        return Mono.fromCallable(() -> {
            try {
                Jwts.parser()
                        .setSigningKey(secret)
                        .parseClaimsJws(token);
                /*Jwts.parser()
                        .setSigningKey(secret.getBytes())
                        .parseClaimsJws(token);*/
                /*Jwts.parserBuilder()
                        .setSigningKey(getSecretKey())
                        .build()
                        .parseClaimsJws(token);*/
                return true;
            } catch (ExpiredJwtException e) {
                System.err.println("JWT token expired: " + e.getMessage());
                return false;
            } catch (MalformedJwtException e) {
                System.err.println("Invalid JWT token format: " + e.getMessage());
                return false;
            } catch (SignatureException e) {
                System.err.println("JWT signature validation failed: " + e.getMessage());
                return false;
            } catch (IllegalArgumentException e) {
                System.err.println("JWT token is empty or null: " + e.getMessage());
                return false;
            } catch (JwtException e) {
                System.err.println("Invalid JWT token: " + e.getMessage());
                return false;
            }
        });
    }

    public Mono<Boolean> isTokenExpired(String token) {
        return extractAllClaims(token)
                .map(claims -> claims.getExpiration().before(new Date()))
                .onErrorResume(e -> {
                    // If we can't extract claims, assume token is invalid/expired
                    System.err.println("Error checking token expiration: " + e.getMessage());
                    return Mono.just(true);
                });
    }

    // Additional method to extract specific claims
    public Mono<Object> getClaim(String token, String claimName) {
        return extractAllClaims(token)
                .map(claims -> claims.get(claimName))
                .onErrorResume(e -> {
                    System.err.println("Error extracting claim " + claimName + ": " + e.getMessage());
                    return Mono.empty();
                });
    }

    // Method to get all claims for debugging
    public Mono<String> debugToken(String token) {
        return extractAllClaims(token)
                .map(claims -> {
                    StringBuilder sb = new StringBuilder();
                    sb.append("JWT Claims:\n");
                    claims.forEach((key, value) -> {
                        sb.append(key).append(": ").append(value).append("\n");
                    });
                    sb.append("Expiration: ").append(claims.getExpiration());
                    sb.append(" (Now: ").append(new Date()).append(")");
                    sb.append(" Is expired: ").append(claims.getExpiration().before(new Date()));
                    return sb.toString();
                })
                .onErrorResume(e -> Mono.just("Error parsing token: " + e.getMessage()));
    }
}