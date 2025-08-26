package com.complete.api.gateway.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;

@Service
public class RedisTokenBucketService {
    private final ReactiveStringRedisTemplate redis;
    private final RedisScript<Long> script;
    private final String keyPrefix;
    private final int capacity;
    private final int refillTokens;
    private final int refillIntervalSeconds;

    public RedisTokenBucketService(
            ReactiveStringRedisTemplate redis,
            @Value("${rate-limiter.key-prefix}") String keyPrefix,
            @Value("${rate-limiter.capacity}") int capacity,
            @Value("${rate-limiter.refill-tokens}") int refillTokens,
            @Value("${rate-limiter.refill-interval-seconds}") int refillIntervalSeconds) {
        this.redis = redis;
        this.keyPrefix = keyPrefix;
        this.capacity = capacity;
        this.refillTokens = refillTokens;
        this.refillIntervalSeconds = refillIntervalSeconds;
        this.script = TokenBucketLua.getScript();
    }

    public Mono<Boolean> tryConsume(String compositeKey) {
        String key = keyPrefix + ":" + compositeKey;
        long now = Instant.now().toEpochMilli();
        String[] keys = new String[]{key};
        // expire a bit longer than one interval so idle buckets vanish
        int expireSeconds = Math.max(refillIntervalSeconds * 2, 60);

        return redis.execute(this.script,
                        java.util.List.of(key),
                        String.valueOf(capacity),
                        String.valueOf(refillTokens),
                        String.valueOf(refillIntervalSeconds * 1000L),
                        String.valueOf(now),
                        String.valueOf(expireSeconds))
                .next()
                .defaultIfEmpty(0L)
                .map(res -> res == 1L);
    }
}
