package com.complete.api.gateway.resolver;

import com.complete.api.gateway.util.JwtUtil;
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
                        if (valid) {
                            return jwtUtil.extractUserId(token)
                                    .map(userId -> userId + "_" + ipAddress)
                                    .onErrorResume(e -> Mono.just("invalid_token_" + ipAddress));
                        } else {
                            return Mono.just("invalid_token_" + ipAddress);
                        }
                    })
                    .onErrorResume(e -> Mono.just("error_" + ipAddress));
        }

        return Mono.just("anonymous_" + ipAddress);
    }
}