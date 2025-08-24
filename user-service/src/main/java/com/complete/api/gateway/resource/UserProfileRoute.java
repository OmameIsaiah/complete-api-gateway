package com.complete.api.gateway.resource;


import com.complete.api.gateway.dto.request.UpdatePasswordRequest;
import com.complete.api.gateway.dto.request.UpdateProfileRequest;
import com.complete.api.gateway.dto.response.ApiResponse;
import com.complete.api.gateway.service.UserProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.complete.api.gateway.util.EndpointsURL.*;


@RestController
@RequestMapping(value = PROFILE_BASE_URL, headers = "Accept=application/json")
@Tag(name = "profile resource", description = "Resource for fetching and updating user profile info, updating password and signing out [Accessible to ALL users with valid authorization]")
@RequiredArgsConstructor
public class UserProfileRoute {
    private final UserProfileService userProfileService;

    @GetMapping(value = PROFILE_INFO, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Resource for fetching user profile info",
            description = "Resource for fetching user profile info"
    )
    @Tag(name = "profile resource")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse> getProfileInfo(HttpServletRequest httpServletRequest) {
        return userProfileService.getProfileInfo(httpServletRequest);
    }

    @PutMapping(value = PROFILE_UPDATE_INFO, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Resource for updating user profile info",
            description = "Resource for updating user profile info"
    )
    @Tag(name = "profile resource")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse> updateProfileInfo(
            @RequestBody @Valid @Schema(
                    description = "Update profile payload",
                    required = true,
                    implementation = UpdateProfileRequest.class) UpdateProfileRequest request,
            HttpServletRequest httpServletRequest) {
        return userProfileService.updateProfileInfo(httpServletRequest, request);
    }

    @PutMapping(value = PROFILE_UPDATE_PASSWORD, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Resource for updating password",
            description = "Resource for updating password"
    )
    @Tag(name = "profile resource")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse> updatePassword(
            @RequestBody @Valid @Schema(
                    description = "Update password payload",
                    required = true,
                    implementation = UpdatePasswordRequest.class) UpdatePasswordRequest request,
            HttpServletRequest httpServletRequest) {
        return userProfileService.updatePassword(httpServletRequest, request);
    }

    @PostMapping(value = PROFILE_SIGNOUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Resource for signing out",
            description = "Resource for signing out"
    )
    @Tag(name = "profile resource")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse> signOut(HttpServletRequest httpServletRequest) {
        return userProfileService.signOut(httpServletRequest);
    }
}
