package com.complete.api.gateway.resource;

import com.complete.api.gateway.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/debug")
public class DebugResource {

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/token")
    public Mono<String> debugToken(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Mono.just("No Authorization header or invalid format");
        }

        String token = authHeader.substring(7);
        return jwtUtil.debugToken(token);
    }

    @GetMapping("/validate")
    public Mono<String> validateToken(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Mono.just("No Authorization header or invalid format");
        }

        String token = authHeader.substring(7);
        return jwtUtil.validateToken(token)
                .map(valid -> "Token is " + (valid ? "VALID" : "INVALID"))
                .onErrorResume(e -> Mono.just("Validation error: " + e.getMessage()));
    }
}