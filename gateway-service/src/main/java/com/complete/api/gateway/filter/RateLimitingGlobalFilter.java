package com.complete.api.gateway.filter;

import com.complete.api.gateway.security.JwtUtil;
import com.complete.api.gateway.service.RedisTokenBucketService;
import com.complete.api.gateway.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Component
@Slf4j
public class RateLimitingGlobalFilter implements GlobalFilter, Ordered {
    private final RedisTokenBucketService tokenBucketService;
    private final JwtUtil jwtUtil;
    private final boolean respectXff;
    private final String denyBody;

    public RateLimitingGlobalFilter(
            RedisTokenBucketService tokenBucketService,
            JwtUtil jwtUtil,
            @Value("${rate-limiter.respect-x-forwarded-for:true}") boolean respectXff,
            @Value("${rate-limiter.deny-body}") String denyBody) {
        this.jwtUtil = jwtUtil;
        this.tokenBucketService = tokenBucketService;
        this.respectXff = respectXff;
        this.denyBody = denyBody;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, org.springframework.cloud.gateway.filter.GatewayFilterChain chain) {

        String ipAddress = Utils.getClientIp(exchange);
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        //Optional<String> userIdOpt = jwtUtil.extractUserIdV2(authHeader);
        //String userId = userIdOpt.orElse("anonymous");
        //String compositeKey = ipAddress + ":" + userId;
        //log.info("##### COMPOSITE KEY: {}", compositeKey);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            log.info("##### AUTHORIZATION PRESENT");
            String token = authHeader.substring(7);
            return jwtUtil.validateToken(token)
                    .flatMap(valid -> {
                        if (Boolean.TRUE.equals(valid)) {
                            Optional<String> userIdOpt = jwtUtil.extractUserIdV2(authHeader);
                            String userId = userIdOpt.orElse("anonymous");
                            return processNumberOfRequestWithRedisAndBlockTooManyRequests((ipAddress + ":" + userId), chain, exchange);
                        } else {
                            log.info("### INVALID JWT TOKEN");
                            return respondUnauthorizedRequests(exchange);
                        }
                    })
                    .onErrorResume(e -> {
                        log.info("### ERROR VALIDATING TOKEN, FALLING BACK TO IP: {}", e.getMessage());
                        return processNumberOfRequestWithRedisAndBlockTooManyRequests((ipAddress + ":" + "token_validation_error"), chain, exchange);
                    });
        } else {
            log.info("##### NO AUTHORIZATION PRESENT");
            return processNumberOfRequestWithRedisAndBlockTooManyRequests((ipAddress + ":" + "anonymous"), chain, exchange);
        }
    }

    private Mono<Void> processNumberOfRequestWithRedisAndBlockTooManyRequests(String compositeKey, GatewayFilterChain chain, ServerWebExchange exchange) {
        return tokenBucketService.tryConsume(compositeKey)
                .flatMap(allowed -> {
                    if (allowed) {
                        log.info("##### ALLOWED: {}", allowed);
                        return chain.filter(exchange);
                    } else {
                        log.info("##### ALLOWED: {}", allowed);
                        return respondTooManyRequests(exchange);
                    }
                });
    }

    private Mono<Void> respondTooManyRequests(ServerWebExchange exchange) {
        var resp = exchange.getResponse();
        resp.setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
        resp.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        byte[] bytes = denyBody.getBytes(StandardCharsets.UTF_8);
        DataBufferFactory bufferFactory = resp.bufferFactory();
        return resp.writeWith(Mono.just(bufferFactory.wrap(bytes)));
    }

    private Mono<Void> respondUnauthorizedRequests(ServerWebExchange exchange) {
        var resp = exchange.getResponse();
        resp.setStatusCode(HttpStatus.UNAUTHORIZED);
        resp.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        byte[] bytes = denyBody.getBytes(StandardCharsets.UTF_8);
        DataBufferFactory bufferFactory = resp.bufferFactory();
        return resp.writeWith(Mono.just(bufferFactory.wrap(bytes)));
    }

    @Override
    public int getOrder() {
        // Run early, before most other filters (but after routing predicate resolution)
        return -100;
    }
}
