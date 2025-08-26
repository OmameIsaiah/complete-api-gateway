package com.complete.api.gateway.resolver;

import com.complete.api.gateway.security.JwtUtil;
import com.complete.api.gateway.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.http.HttpHeaders;
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
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith(Utils.TOKEN_PREFIX)) {
            Optional<String> userIdOpt = jwtUtil.extractUserIdV2(authHeader);
            String userId = userIdOpt.orElse(Utils.ANONYMOUS_USER_ID);
            return Mono.just(userId + ipAddress);
        } else {
            return Mono.just(Utils.ANONYMOUS_USER_ID + ipAddress);
        }
    }
}