package com.complete.api.gateway.service;

import com.complete.api.gateway.dto.request.UpdatePasswordRequest;
import com.complete.api.gateway.dto.request.UpdateProfileRequest;
import com.complete.api.gateway.dto.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;


public interface UserProfileService {
    ResponseEntity<ApiResponse> getProfileInfo(HttpServletRequest httpServletRequest);

    ResponseEntity<ApiResponse> updateProfileInfo(HttpServletRequest httpServletRequest, UpdateProfileRequest request);

    ResponseEntity<ApiResponse> updatePassword(HttpServletRequest httpServletRequest, UpdatePasswordRequest request);

    ResponseEntity<ApiResponse> signOut(HttpServletRequest httpServletRequest);
}
