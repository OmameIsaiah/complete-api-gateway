package com.complete.api.gateway.resource;


import com.complete.api.gateway.dto.request.RoleRequest;
import com.complete.api.gateway.dto.request.RoleUpdateRequest;
import com.complete.api.gateway.dto.response.ApiResponse;
import com.complete.api.gateway.service.RoleAndPermissionsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.complete.api.gateway.util.EndpointsURL.*;


@RestController
@RequestMapping(value = ROLES_BASE_URL, headers = "Accept=application/json")
@Tag(name = "roles and permissions resource", description = "Resource for creating, updating and viewing roles and permissions [Accessible only to ADMIN users with valid authorization]")
@RequiredArgsConstructor
public class RoleAndPermissionsRoute {
    private final RoleAndPermissionsService roleAndPermissionsService;

    @GetMapping(value = VIEW_PERMISSIONS, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Resource for viewing list of permissions",
            description = "Resource for viewing list of permissions"
    )
    @Tag(name = "roles and permissions resource")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse> viewPermissions() {
        return roleAndPermissionsService.viewPermissions();
    }

    @PostMapping(value = ADD_NEW_ROLE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Resource for adding new custom role",
            description = "Resource for adding new custom role"
    )
    @Tag(name = "roles and permissions resource")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse> addRole(
            @RequestBody @Valid @Schema(
                    description = "Add role payload",
                    required = true,
                    implementation = RoleRequest.class) RoleRequest request) {
        return roleAndPermissionsService.addRole(request);
    }

    @PutMapping(value = UPDATE_ROLE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Resource for updating existing role via the role uuid",
            description = "Resource for updating existing role via the role uuid"
    )
    @Tag(name = "roles and permissions resource")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse> updateRole(
            @RequestBody @Valid @Schema(
                    description = "Update role payload",
                    required = true,
                    implementation = RoleUpdateRequest.class) RoleUpdateRequest request) {
        return roleAndPermissionsService.updateRole(request);
    }

    @GetMapping(value = VIEW_ALL_ROLES, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Resource for viewing all roles",
            description = "Resource for viewing all roles"
    )
    @Tag(name = "roles and permissions resource")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse> viewAllRoles() {
        return roleAndPermissionsService.viewAllRoles();
    }

    @GetMapping(value = VIEW_ROLE_BY_UUID, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Resource for viewing role by uuid",
            description = "Resource for viewing role by uuid"
    )
    @Tag(name = "roles and permissions resource")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse> viewRoleByID(
            @Parameter(
                    name = "uuid",
                    description = "ID of the user to retrieve",
                    required = true,
                    example = "18ebdf54-5ee5-4b4d-8ea6-64c407638012")
            @PathVariable("uuid") String uuid) {
        return roleAndPermissionsService.viewRoleByID(uuid);
    }
}
