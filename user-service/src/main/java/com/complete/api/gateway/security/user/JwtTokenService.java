package com.complete.api.gateway.security.user;


import com.complete.api.gateway.security.jwt.JwtResponse;

public interface JwtTokenService {
    JwtResponse getAccessToken(String username, String password);
}
