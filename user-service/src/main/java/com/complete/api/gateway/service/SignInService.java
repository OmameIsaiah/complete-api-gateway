package com.complete.api.gateway.service;

import com.complete.api.gateway.dto.request.Credentials;
import com.complete.api.gateway.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface SignInService {
    ResponseEntity<ApiResponse> signIn(Credentials credentials);
}
