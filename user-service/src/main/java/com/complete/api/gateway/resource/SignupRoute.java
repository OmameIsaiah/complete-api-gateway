package com.complete.api.gateway.resource;


import com.complete.api.gateway.dto.request.SignUpRequest;
import com.complete.api.gateway.dto.response.ApiResponse;
import com.complete.api.gateway.service.SignUpService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.complete.api.gateway.util.EndpointsURL.ONBOARDING_BASE_URL;
import static com.complete.api.gateway.util.EndpointsURL.ONBOARDING_SIGNUP;


@RestController
@RequestMapping(value = ONBOARDING_BASE_URL, headers = "Accept=application/json")
@Tag(name = "onboarding resource", description = "Resource for creating new user account and verifying the account [Accessible to PUBLIC, authorization NOT required]")
@RequiredArgsConstructor
public class SignupRoute {
    private final SignUpService signUpService;

    @PostMapping(value = ONBOARDING_SIGNUP, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Resource for creating new user account",
            description = "Resource for creating new user account"
    )
    @Tag(name = "onboarding resource")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponse> signUp(
            @RequestBody @Valid @Schema(
                    description = "Signup payload",
                    required = true,
                    implementation = SignUpRequest.class) SignUpRequest request) {
        return signUpService.signUp(request);
    }
}
