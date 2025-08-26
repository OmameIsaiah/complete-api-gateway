package com.complete.api.gateway.filter;

import com.complete.api.gateway.resolver.RateLimiterKeyResolver;
import com.complete.api.gateway.service.TokenBucketRateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

@Component
public class RateLimiterFilter extends AbstractGatewayFilterFactory<RateLimiterFilter.Config> {
    @Autowired
    private TokenBucketRateLimiter rateLimiter;
    @Autowired
    private RateLimiterKeyResolver keyResolver;
    @Value("${rate-limiter.deny-body}")
    private String denyBody;

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> chain.filter(exchange);
    }

    public static class Config {
        // Configuration properties if needed
    }
}