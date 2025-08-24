package com.complete.api.gateway.service;

import com.complete.api.gateway.dto.response.ApiResponse;
import com.complete.api.gateway.enums.UserType;
import org.springframework.http.ResponseEntity;

public interface UserManagementService {
    ResponseEntity<ApiResponse> getAllUsers(Integer page, Integer size);

    ResponseEntity<ApiResponse> filterUsers(Integer page, Integer size, UserType userType);

    ResponseEntity<ApiResponse> searchUsers(String keyword);

    ResponseEntity<ApiResponse> deleteUser(String uuid);
}
