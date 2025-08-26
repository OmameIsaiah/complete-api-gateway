package com.complete.api.gateway.filter;

import com.complete.api.gateway.resolver.RateLimiterKeyResolver;
import com.complete.api.gateway.service.TokenBucketRateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

//@Component
public class RateLimiterFilter {
    /*
    extends AbstractGatewayFilterFactory<RateLimiterFilter.Config>
} {
    @Autowired
    private TokenBucketRateLimiter rateLimiter;
    @Autowired
    private RateLimiterKeyResolver keyResolver;
    @Value("${rate-limiter.deny-body}")
    private String denyBody;

*//*    public RateLimiterFilter(TokenBucketRateLimiter rateLimiter,
                             RateLimiterKeyResolver keyResolver,
                             @Value("${rate-limiter.deny-body}") String denyBody) {
        super(Config.class);
        this.rateLimiter = rateLimiter;
        this.keyResolver = keyResolver;
        this.denyBody = denyBody;
    }*//*

    @Override
    public GatewayFilter apply(Config config) {
        System.out.println("### LOG HERE ...");
        return (exchange, chain) -> keyResolver.resolve(exchange)
                .flatMap(key -> {
                    System.out.println("### TOKEN KEY: " + key);
                    return rateLimiter.allowRequest(key);
                })
                .flatMap(allowed -> {
                    if (allowed) {
                        return chain.filter(exchange);
                    } else {
                        System.out.println("######## THIS REQUEST IS BLOCKED...");
                        //throw new TooManyRequestException("Oops! Rate limit exceeded, please try again in 30 seconds.");
                        return processTooManyRequestResponse(exchange);
                    }
                });
    }

    private Mono<Void> processTooManyRequestResponse(ServerWebExchange exchange) {
        return keyResolver.resolve(exchange)
                .flatMap(key -> rateLimiter.getResetTime(key)
                        .flatMap(resetTime -> {
                            ServerHttpResponse response = exchange.getResponse();
                            response.setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
                            response.getHeaders().add("X-RateLimit-Retry-After", String.valueOf(resetTime));
                            response.getHeaders().add("X-RateLimit-Limit", "100");

                            *//*String errorMessage = String.format(
                                    "Rate limit exceeded. Please try again in %d seconds.",
                                    resetTime
                            );
                            byte[] bytes = errorMessage.getBytes();
                            *//*
                            byte[] bytes = denyBody.getBytes(StandardCharsets.UTF_8);
                            response.getHeaders().setContentLength(bytes.length);
                            return response.writeWith(Mono.just(response.bufferFactory().wrap(bytes)));
                        }));
    }

    public static class Config {
        // Configuration properties if needed
    }*/
}