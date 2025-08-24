package com.complete.api.gateway.security.jwt;


import com.complete.api.gateway.security.user.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;


@Slf4j
@Component
public class AuthTokenFilter extends OncePerRequestFilter {
    @Value("${app.jwt.header.string}")
    public String HEADER_STRING;

    @Value("${app.jwt.token.prefix}")
    public String TOKEN_PREFIX;
    private final JwtUtils jwtUtils;
    private final UserDetailsServiceImpl userDetailsService;

    public AuthTokenFilter(JwtUtils jwtUtils, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {

        if (shouldSkipAuthentication(req)) {
            chain.doFilter(req, res);
            return;
        }

        String header = req.getHeader(HEADER_STRING);
        String username = null;
        String authToken = null;
        if (Objects.nonNull(header) && header.startsWith(TOKEN_PREFIX)) {
            authToken = header.replace(TOKEN_PREFIX, "").replaceAll(" ", "");
            username = jwtUtils.getUserNameFromJwtToken(authToken);
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (jwtUtils.validateToken(authToken, userDetails)) {
                UsernamePasswordAuthenticationToken authentication = jwtUtils.getAuthenticationToken(authToken, SecurityContextHolder.getContext().getAuthentication(), userDetails);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                log.info("AUTHENTICATED USER: {}", username);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        chain.doFilter(req, res);
    }

    private static final String[] AUTH_WHITELIST = {
            "/api-docs/**",
            "/v2/api-docs/**",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html/**",
            "/swagger-ui/**",
            "/webjars/**",
            "/console/**",
            "/h2-console/**",
            "/api/v1/users/onboarding/**",
            "/api/v1/auth/**",
            "/api/v1/users/entrance/**"
    };

    private boolean shouldSkipAuthentication(HttpServletRequest request) {
        AntPathMatcher pathMatcher = new AntPathMatcher();
        String path = request.getServletPath();
        boolean shouldSkip = Arrays.stream(AUTH_WHITELIST)
                .anyMatch(pattern -> pathMatcher.match(pattern, path));
        //log.info("Checking if path should skip auth: {} => {}", path, shouldSkip);
        return shouldSkip;
    }


    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(TOKEN_PREFIX + " ")) {
            return headerAuth.substring(7, headerAuth.length());
        }
        return null;
    }
}