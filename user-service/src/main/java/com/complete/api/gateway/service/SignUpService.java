package com.complete.api.gateway.service;


import com.complete.api.gateway.dto.request.SignUpRequest;
import com.complete.api.gateway.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface SignUpService {
    ResponseEntity<ApiResponse> signUp(SignUpRequest request);
}
