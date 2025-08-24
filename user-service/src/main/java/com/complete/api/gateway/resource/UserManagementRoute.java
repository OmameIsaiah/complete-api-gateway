package com.complete.api.gateway.resource;


import com.complete.api.gateway.dto.response.ApiResponse;
import com.complete.api.gateway.enums.UserType;
import com.complete.api.gateway.service.UserManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.complete.api.gateway.util.EndpointsURL.*;

@RestController
@RequestMapping(value = USER_MANAGEMENT_BASE_URL, headers = "Accept=application/json")
@Tag(name = "user management resource", description = "Resource for searching, filtering and deleting users [Accessible only to ADMIN users with valid authorization]")
@RequiredArgsConstructor
public class UserManagementRoute {
    private final UserManagementService userManagementService;

    @GetMapping(value = USER_MANAGEMENT_FIND_ALL, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Resource for fetching all users",
            description = "Resource for fetching all users"
    )
    @Tag(name = "user management resource")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse> getAllUsers(
            @Parameter(
                    name = "page",
                    description = "Page to fetch",
                    required = false,
                    example = "0")
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @Parameter(
                    name = "size",
                    description = "Size of the page to fetch",
                    required = false,
                    example = "0")
            @RequestParam(value = "size", defaultValue = "50") Integer size) {
        return userManagementService.getAllUsers(page, size);
    }

    @GetMapping(value = USER_MANAGEMENT_FILTER, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Resource for filtering users",
            description = "Resource for filtering users"
    )
    @Tag(name = "user management resource")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse> filterUsers(
            @Parameter(
                    name = "page",
                    description = "Page to fetch",
                    required = false,
                    example = "0")
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @Parameter(
                    name = "size",
                    description = "Size of the page to fetch",
                    required = false,
                    example = "0")
            @RequestParam(value = "size", defaultValue = "50") Integer size,
            @Parameter(
                    name = "userType",
                    description = "User type",
                    required = true,
                    example = "USER")
            @RequestParam(value = "userType", defaultValue = "USER") UserType userType) {
        return userManagementService.filterUsers(page, size, userType);
    }

    @GetMapping(value = USER_MANAGEMENT_SEARCH, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Resource for searching users by name, email or user type",
            description = "Resource for searching users by name, email or user type"
    )
    @Tag(name = "user management resource")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse> searchUsers(
            @Parameter(
                    name = "keyword",
                    description = "Keyword to search",
                    required = true,
                    example = "john")
            @RequestParam("keyword") String keyword) {
        return userManagementService.searchUsers(keyword);
    }

    @DeleteMapping(value = USER_MANAGEMENT_DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Resource for deleting a user by uuid",
            description = "Resource for deleting a user by uuid"
    )
    @Tag(name = "user management resource")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse> deleteUser(
            @Parameter(
                    name = "uuid",
                    description = "Uuid of the user to delete",
                    required = true,
                    example = "18ebdf54-5ee5-4b4d-8ea6-64c407638012")
            @PathVariable("uuid") String uuid) {
        return userManagementService.deleteUser(uuid);
    }
}
