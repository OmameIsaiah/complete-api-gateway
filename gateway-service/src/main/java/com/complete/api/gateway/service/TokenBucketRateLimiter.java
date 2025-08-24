package com.complete.api.gateway.service;

import com.complete.api.gateway.exceptions.TooManyRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Service
public class TokenBucketRateLimiter {
    @Autowired
    @Qualifier("reactiveRedisTemplate")
    private ReactiveRedisTemplate<String, String> redisTemplate;

    //@Value("${rate-limiter.capacity:100}")
    @Value("${rate-limiter.capacity}")
    private int capacity;

    //@Value("${rate-limiter.refill-period:30}")
    @Value("${rate-limiter.refill-period}")
    private int refillPeriod;

    //@Value("${rate-limiter.refill-period-unit:SECONDS}")
    @Value("${rate-limiter.refill-period-unit}")
    private String refillPeriodUnit;

    //@Value("${rate-limiter.tokens-per-refill:100}")
    @Value("${rate-limiter.tokens-per-refill}")
    private int tokensPerRefill;

    /*public Mono<Boolean> allowRequest(String key) {
        String tokensKey = "rate_limiter:tokens:" + key;
        String timestampKey = "rate_limiter:timestamp:" + key;

        return redisTemplate.opsForValue().get(tokensKey)
                .defaultIfEmpty("")
                .flatMap(currentTokensStr ->
                        redisTemplate.opsForValue().get(timestampKey)
                                .defaultIfEmpty("")
                                .flatMap(lastRefillStr -> {
                                    long currentTokens = currentTokensStr.isEmpty() ? capacity : Long.parseLong(currentTokensStr);
                                    long lastRefillTimestamp = lastRefillStr.isEmpty() ? Instant.now().getEpochSecond() : Long.parseLong(lastRefillStr);
                                    long now = Instant.now().getEpochSecond();

                                    long timePassed = now - lastRefillTimestamp;
                                    long refillAmount = (timePassed * tokensPerRefill) / getRefillPeriodInSeconds();
                                    long newTokens = Math.min(capacity, currentTokens + refillAmount);

                                    if (newTokens >= 1) {
                                        newTokens--;
                                        long expirationTime = getRefillPeriodInSeconds() * 2;

                                        return redisTemplate.opsForValue().set(tokensKey, String.valueOf(newTokens), java.time.Duration.ofSeconds(expirationTime))
                                                .then(redisTemplate.opsForValue().set(timestampKey, String.valueOf(now), java.time.Duration.ofSeconds(expirationTime)))
                                                .then(Mono.just(true));
                                    } else {
                                        return Mono.just(false);
                                    }
                                })
                );
    }*/

    public Mono<Boolean> allowRequest(String key) {
        String tokensKey = "rate_limiter:tokens:" + key;
        String timestampKey = "rate_limiter:timestamp:" + key;

        return redisTemplate.opsForValue().get(tokensKey)
                .defaultIfEmpty("")
                .flatMap(currentTokensStr ->
                        redisTemplate.opsForValue().get(timestampKey)
                                .defaultIfEmpty("")
                                .flatMap(lastRefillStr -> {
                                    long currentTokens = currentTokensStr.isEmpty() ? capacity : Long.parseLong(currentTokensStr);
                                    long lastRefillTimestamp = lastRefillStr.isEmpty() ? Instant.now().getEpochSecond() : Long.parseLong(lastRefillStr);
                                    long now = Instant.now().getEpochSecond();

                                    // Calculate time passed since last refill
                                    long timePassed = now - lastRefillTimestamp;

                                    // Refill tokens based on time passed
                                    long refillAmount = (timePassed * tokensPerRefill) / getRefillPeriodInSeconds();
                                    long newTokens = Math.min(capacity, currentTokens + refillAmount);

                                    System.out.println("Key: " + key + ", Current: " + currentTokens +
                                            ", Refill: " + refillAmount + ", New: " + newTokens);

                                    if (newTokens >= 1) {
                                        newTokens--;

                                        long expirationTime = getRefillPeriodInSeconds() * 2;
                                        return redisTemplate.opsForValue().set(tokensKey, String.valueOf(newTokens),
                                                        java.time.Duration.ofSeconds(expirationTime))
                                                .then(redisTemplate.opsForValue().set(timestampKey, String.valueOf(now),
                                                        java.time.Duration.ofSeconds(expirationTime)))
                                                .then(Mono.just(true));
                                    } else {
                                        System.out.println("Rate limit exceeded for key: " + key);
                                        throw new TooManyRequestException("Oops! Rate limit exceeded, please try again in 30 seconds.");
                                        //return Mono.just(false);
                                    }
                                })
                );
    }

    private long getRefillPeriodInSeconds() {
        return switch (refillPeriodUnit.toUpperCase()) {
            case "SECONDS" -> refillPeriod;
            case "MINUTES" -> refillPeriod * 60;
            case "HOURS" -> refillPeriod * 3600;
            default -> refillPeriod;
        };
    }

    public Mono<Long> getRemainingTokens(String key) {
        String tokensKey = "rate_limiter:tokens:" + key;
        return redisTemplate.opsForValue().get(tokensKey)
                .map(Long::parseLong)
                .defaultIfEmpty((long) capacity);
    }

    public Mono<Long> getResetTime(String key) {
        String timestampKey = "rate_limiter:timestamp:" + key;
        return redisTemplate.opsForValue().get(timestampKey)
                .map(lastRefillStr -> {
                    long lastRefillTimestamp = Long.parseLong(lastRefillStr);
                    long now = Instant.now().getEpochSecond();
                    long nextRefill = lastRefillTimestamp + getRefillPeriodInSeconds();
                    return Math.max(0, nextRefill - now);
                })
                .defaultIfEmpty(0L);
    }
}