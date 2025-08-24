package com.complete.api.gateway.service.impl;

import com.complete.api.gateway.dto.request.Credentials;
import com.complete.api.gateway.dto.response.ApiResponse;
import com.complete.api.gateway.dto.response.JwtData;
import com.complete.api.gateway.dto.response.SignInResponse;
import com.complete.api.gateway.dto.response.UserData;
import com.complete.api.gateway.exceptions.BadRequestException;
import com.complete.api.gateway.exceptions.RecordNotFoundException;
import com.complete.api.gateway.model.UsersTable;
import com.complete.api.gateway.repository.UserRepository;
import com.complete.api.gateway.security.jwt.JwtResponse;
import com.complete.api.gateway.security.user.JwtTokenService;
import com.complete.api.gateway.service.SignInService;
import com.complete.api.gateway.util.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.complete.api.gateway.util.AppMessages.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class SignInServiceImpl implements SignInService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;

    private UsersTable validateUserByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RecordNotFoundException(WRONG_ACCOUNT_EMAIL));
    }

    @Override
    public ResponseEntity<ApiResponse> signIn(Credentials credentials) {
        UsersTable users = validateSignInParamAndPassword(credentials);
        UserData userData = getUserData(users);
        JwtResponse jwtResponse = jwtTokenService.getAccessToken(credentials.getEmail(), credentials.getPassword());
        log.info("####### SIGN IN SUCCESSFUL FOR {}", userData.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(true,
                        HttpStatus.OK.value(),
                        HttpStatus.OK,
                        SIGN_IN_SUCCESSFULLY,
                        SignInResponse.builder()
                                .userAccountData(userData)
                                .jwtTokenData(JwtData.builder()
                                        .authorizationToken(jwtResponse.getAuthorizationToken())
                                        .type(jwtResponse.getType())
                                        .roles(jwtResponse.getRoles())
                                        .permissions(jwtResponse.getPermissions())
                                        .build())
                                .build()
                ));
    }

    private UserData getUserData(UsersTable users) {
        users.setIsOnline(true);
        users.setLastLogin(LocalDateTime.now());
        userRepository.save(users);
        return UserData.builder()
                .uuid(users.getUuid())
                .name(users.getName())
                .email(users.getEmail())
                .userType(users.getUserType())
                .userToken(users.getUserToken())
                .isOnline(users.getIsOnline())
                .lastLogin(Utils.convertLocalDateTimeToString(users.getLastLogin()))
                .build();
    }

    private UsersTable validateSignInParamAndPassword(Credentials credentials) {
        if (Objects.isNull(credentials)) {
            throw new BadRequestException(INVALID_REQUEST_PARAMETERS);
        }
        if (Objects.isNull(credentials.getEmail()) || "".equals(credentials.getEmail())) {
            throw new BadRequestException(NULL_EMAIL);
        }
        if (Objects.isNull(credentials.getPassword()) || "".equals(credentials.getPassword())) {
            throw new BadRequestException(NULL_PASSWORD);
        }
        UsersTable users = validateUserByEmail(credentials.getEmail());
        if (Objects.isNull(users.getVerified()) || !users.getVerified()) {
            throw new BadRequestException(ACCOUNT_NOT_VERIFIED);
        }
        if (!passwordEncoder.matches(credentials.getPassword(), users.getPassword())) {
            throw new BadRequestException(WRONG_ACCOUNT_PASSWORD);
        }
        return users;
    }
}
