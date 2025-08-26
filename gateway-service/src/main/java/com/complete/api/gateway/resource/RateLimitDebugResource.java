package com.complete.api.gateway.resource;

import com.complete.api.gateway.security.JwtUtil;
import com.complete.api.gateway.service.TokenBucketRateLimiter;
import com.complete.api.gateway.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/debug-rate-limit")
public class RateLimitDebugResource {
    @Autowired
    private TokenBucketRateLimiter rateLimiter;

    @Autowired
    private JwtUtil jwtUtil;


    @GetMapping("/get-limit-status")
    public Mono<String> getRateLimitStatus(@RequestHeader(value = "Authorization", required = false) String authHeader,
                                           ServerWebExchange exchange) {
        String ipAddress = Utils.getClientIp(exchange);

        Mono<String> userIdMono;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            userIdMono = jwtUtil.extractUserId(token)
                    .onErrorResume(e -> {
                        System.err.println("Error extracting user ID: " + e.getMessage());
                        return Mono.just("error");
                    });
        } else {
            userIdMono = Mono.just("anonymous");
        }

        return userIdMono.flatMap(userId -> {
            String key = userId + "_" + ipAddress;

            return Mono.zip(
                    rateLimiter.getRemainingTokens(key),
                    rateLimiter.getResetTime(key),
                    rateLimiter.allowRequest(key)
            ).map(tuple -> {
                long remaining = tuple.getT1();
                long resetIn = tuple.getT2();
                boolean allowed = tuple.getT3();

                return String.format(
                        "Rate Limit Status:\n" +
                                "Key: %s\n" +
                                "Remaining tokens: %d\n" +
                                "Reset in: %d seconds\n" +
                                "Next request allowed: %s",
                        key, remaining, resetIn, allowed
                );
            });
        });
    }

    @GetMapping("/test-rate-limit")
    public Mono<String> testRateLimit(@RequestHeader(value = "Authorization", required = false) String authHeader,
                                      ServerWebExchange exchange) {
        String ipAddress = Utils.getClientIp(exchange);

        Mono<String> userIdMono;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            userIdMono = jwtUtil.extractUserId(token)
                    .onErrorResume(e -> {
                        System.err.println("Error extracting user ID: " + e.getMessage());
                        return Mono.just("error");
                    });
        } else {
            userIdMono = Mono.just("anonymous");
        }

        return userIdMono.flatMap(userId -> {
            String key = userId + "_" + ipAddress;

            return rateLimiter.allowRequest(key)
                    .map(allowed -> {
                        if (allowed) {
                            return "✅ Request ALLOWED for key: " + key;
                        } else {
                            return "❌ Request DENIED for key: " + key + " (Rate limit exceeded)";
                        }
                    });
        });
    }

    @GetMapping("/simulate")
    public Mono<String> simulateRequests(@RequestHeader(value = "Authorization", required = false) String authHeader,
                                         @RequestParam(defaultValue = "10") int count,
                                         ServerWebExchange exchange) {
        String ipAddress = Utils.getClientIp(exchange);
        Mono<String> userIdMono;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            userIdMono = jwtUtil.extractUserId(token)
                    .onErrorResume(e -> {
                        System.err.println("Error extracting user ID: " + e.getMessage());
                        return Mono.just("error");
                    });
        } else {
            userIdMono = Mono.just("anonymous");
        }

        return userIdMono.flatMap(userId -> {
            String key = userId + "_" + ipAddress;

            // Get initial state
            return rateLimiter.getRemainingTokens(key)
                    .flatMap(initialTokens -> {
                        StringBuilder result = new StringBuilder();
                        result.append("Simulating ").append(count).append(" requests for key: ").append(key).append("\n");
                        result.append("Initial tokens: ").append(initialTokens).append("\n\n");

                        // Create a flux to simulate multiple requests
                        return Flux.range(1, count)
                                .flatMap(i -> rateLimiter.allowRequest(key)
                                        .map(allowed -> {
                                            result.append("Request ").append(i)
                                                    .append(": ").append(allowed ? "✅" : "❌")
                                                    .append("\n");
                                            return allowed;
                                        }), 5) // concurrency of 5
                                .then(Mono.defer(() -> rateLimiter.getRemainingTokens(key)
                                        .map(finalTokens -> {
                                            result.append("\nFinal tokens: ").append(finalTokens);
                                            return result.toString();
                                        })
                                ));
                    });
        });
    }
}