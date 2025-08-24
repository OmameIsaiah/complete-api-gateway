package com.complete.api.gateway.resource;


import com.complete.api.gateway.dto.request.Credentials;
import com.complete.api.gateway.dto.response.ApiResponse;
import com.complete.api.gateway.service.SignInService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.complete.api.gateway.util.EndpointsURL.ENTRANCE_BASE_URL;
import static com.complete.api.gateway.util.EndpointsURL.ENTRANCE_SIGNIN;

@RestController
@RequestMapping(value = ENTRANCE_BASE_URL, headers = "Accept=application/json")
@Tag(name = "entrance resource", description = "Resource for user sign in [Accessible to ALL users, authorization NOT required]")
@RequiredArgsConstructor
public class SigninRoute {
    private final SignInService signInService;

    @PostMapping(value = ENTRANCE_SIGNIN, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Resource for user sign in",
            description = "Resource for user sign in"
    )
    @Tag(name = "entrance resource")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse> signIn(
            @RequestBody @Valid @Schema(
                    description = "Signin payload",
                    required = true,
                    implementation = Credentials.class) Credentials credentials) {
        return signInService.signIn(credentials);
    }
}
