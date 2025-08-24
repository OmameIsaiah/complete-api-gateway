package com.complete.api.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    private static final String[] AUTH_WHITELIST = {
            "/api-docs/**",
            "/v2/api-docs/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html/**",
            "/swagger-ui/**",
            "/management/health",
            "/management/health/**",
            "/management/info",
            "/management/prometheus",
            "/api/v1/test/**",
            "/api/v1/users/account/**",
            "/services/**"
    };

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(AUTH_WHITELIST).permitAll()
                        //.anyExchange().permitAll() // Adjust based on your needs
                        .pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .pathMatchers("/api/v1/users/**").authenticated()
                        .pathMatchers("/api/v1/products/**").authenticated()
                )
                .build();
    }
}