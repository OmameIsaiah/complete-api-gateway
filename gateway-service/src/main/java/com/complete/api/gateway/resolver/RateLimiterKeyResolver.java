package com.complete.api.gateway.resolver;

import com.complete.api.gateway.security.JwtUtil;
import com.complete.api.gateway.utils.Utils;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

//@Component
public class RateLimiterKeyResolver {
    /*
        implements KeyResolver {

    private final JwtUtil jwtUtil;

    public RateLimiterKeyResolver(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<String> resolve(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        //String ipAddress = getClientIp(request);
        String ipAddress = Utils.getClientIp(exchange);
        String authHeader = request.getHeaders().getFirst("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            return jwtUtil.validateToken(token)
                    .flatMap(valid -> {
                        if (Boolean.TRUE.equals(valid)) {
                            return jwtUtil.extractUserId(token)
                                    .map(userId -> {
                                        String rateLimitKey = userId + "_" + ipAddress;
                                        System.out.println("Rate limiting key: " + rateLimitKey);
                                        return rateLimitKey;
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
        //return Mono.just("anonymous_" + ipAddress);
        System.out.println("### TOKEN KEY: " + "user_" + ipAddress);
        return Mono.just("user_" + ipAddress);
    }

    *//*private String getClientIp(ServerHttpRequest request) {
        String ipAddress = "unknown";
        try {
            InetSocketAddress remoteAddress = request.getRemoteAddress();
            if (remoteAddress != null && remoteAddress.getAddress() != null) {
                ipAddress = remoteAddress.getAddress().getHostAddress();

                // Handle IPv6 localhost (convert to IPv4 for consistency)
                if ("0:0:0:0:0:0:0:1".equals(ipAddress) || "::1".equals(ipAddress)) {
                    ipAddress = "127.0.0.1";
                }
            }
        } catch (Exception e) {
            System.err.println("Error getting client IP: " + e.getMessage());
        }
        return ipAddress;
    }*//*

     *//*private String getClientIp(ServerWebExchange exchange) {
        try {
            InetSocketAddress remoteAddress = exchange.getRequest().getRemoteAddress();
            if (remoteAddress != null && remoteAddress.getAddress() != null) {
                String ipAddress = remoteAddress.getAddress().getHostAddress();
                // Handle IPv6 localhost
                if ("0:0:0:0:0:0:0:1".equals(ipAddress) || "::1".equals(ipAddress)) {
                    return "127.0.0.1";
                }
                return ipAddress;
            }
        } catch (Exception e) {
            System.err.println("Error getting client IP: " + e.getMessage());
        }
        return "unknown";
    }*/
}