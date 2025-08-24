package com.complete.api.gateway.service;

import com.complete.api.gateway.dto.request.RoleRequest;
import com.complete.api.gateway.dto.request.RoleUpdateRequest;
import com.complete.api.gateway.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface RoleAndPermissionsService {
    ResponseEntity<ApiResponse> viewPermissions();

    ResponseEntity<ApiResponse> addRole(RoleRequest request);

    ResponseEntity<ApiResponse> updateRole(RoleUpdateRequest request);

    ResponseEntity<ApiResponse> viewAllRoles();

    ResponseEntity<ApiResponse> viewRoleByID(String uuid);
}