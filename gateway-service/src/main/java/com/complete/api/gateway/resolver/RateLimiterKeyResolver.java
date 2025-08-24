package com.complete.api.gateway.resolver;

import com.complete.api.gateway.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
public class RateLimiterKeyResolver implements KeyResolver {
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Mono<String> resolve(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        String ipAddress = Objects.requireNonNull(request.getRemoteAddress()).getAddress().getHostAddress();

        String authHeader = request.getHeaders().getFirst("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            return jwtUtil.validateToken(token)
                    .flatMap(valid -> {
                        if (Boolean.TRUE.equals(valid)) {
                            return jwtUtil.extractUserId(token)
                                    .map(userId -> {
                                        System.out.println("Rate limiting for user: " + userId + " from IP: " + ipAddress);
                                        return userId + "_" + ipAddress;
                                    })
                                    .onErrorResume(e -> {
                                        System.err.println("Error extracting user ID, falling back to IP: " + e.getMessage());
                                        return Mono.just("error_" + ipAddress);
                                    });
                        } else {
                            System.err.println("Invalid JWT token, falling back to IP-based rate limiting");
                            return Mono.just("invalid_token_" + ipAddress);
                        }
                    })
                    .onErrorResume(e -> {
                        System.err.println("Error validating token, falling back to IP: " + e.getMessage());
                        return Mono.just("validation_error_" + ipAddress);
                    });
        }

        System.out.println("No JWT token found, using IP-based rate limiting: " + ipAddress);
        return Mono.just("anonymous_" + ipAddress);
    }
}