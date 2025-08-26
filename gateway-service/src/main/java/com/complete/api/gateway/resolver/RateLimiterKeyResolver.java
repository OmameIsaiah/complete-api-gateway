package com.complete.api.gateway.resolver;

import com.complete.api.gateway.security.JwtUtil;
import com.complete.api.gateway.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
@Slf4j
public class RateLimiterKeyResolver implements KeyResolver {
    private final JwtUtil jwtUtil;

    public RateLimiterKeyResolver(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<String> resolve(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        String ipAddress = Utils.getClientIp(exchange);
        String authHeader = request.getHeaders().getFirst("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            Optional<String> userIdOpt = jwtUtil.extractUserIdV2(authHeader);
            String userId = userIdOpt.orElse("anonymous");
            return Mono.just(userId + ipAddress);
        } else {
            return Mono.just("anonymous" + ipAddress);
        }

/*
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            return jwtUtil.validateToken(token)
                    .flatMap(valid -> {
                        if (Boolean.TRUE.equals(valid)) {
                            return jwtUtil.extractUserId(token)
                                    .map(userId -> {
                                        String rateLimitKey = userId + "_" + ipAddress;
                                        log.info("Rate limiting key: " + rateLimitKey);
                                        return rateLimitKey;
                                    })
                                    .onErrorResume(e -> {
                                        log.info("Error extracting user ID, falling back to IP: " + e.getMessage());
                                        return Mono.just("error_" + ipAddress);
                                    });
                        } else {
                            log.info("Invalid JWT token, falling back to IP-based rate limiting");
                            return Mono.just("invalid_token_" + ipAddress);
                        }
                    })
                    .onErrorResume(e -> {
                        log.info("Error validating token, falling back to IP: " + e.getMessage());
                        return Mono.just("validation_error_" + ipAddress);
                    });
        }
        log.info("No JWT token found, using IP-based rate limiting: " + ipAddress);
        System.out.println("### TOKEN KEY: " + "user_" + ipAddress);
        return Mono.just("user_" + ipAddress);*/
    }
}