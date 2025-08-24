package com.complete.api.gateway.service.impl;

import com.complete.api.gateway.dto.request.SignUpRequest;
import com.complete.api.gateway.dto.response.ApiResponse;
import com.complete.api.gateway.enums.DefaultRoles;
import com.complete.api.gateway.enums.UserType;
import com.complete.api.gateway.exceptions.BadRequestException;
import com.complete.api.gateway.exceptions.DuplicateRecordException;
import com.complete.api.gateway.exceptions.RecordNotFoundException;
import com.complete.api.gateway.model.Role;
import com.complete.api.gateway.model.UserRole;
import com.complete.api.gateway.model.UsersTable;
import com.complete.api.gateway.repository.RoleRepository;
import com.complete.api.gateway.repository.UserRepository;
import com.complete.api.gateway.repository.UserRoleRepository;
import com.complete.api.gateway.service.SignUpService;
import com.complete.api.gateway.util.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.complete.api.gateway.util.AppMessages.*;


@Service
@RequiredArgsConstructor
public class SignUpServiceImpl implements SignUpService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    private UsersTable validateUserByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RecordNotFoundException(WRONG_ACCOUNT_EMAIL));
    }

    @Override
    public ResponseEntity<ApiResponse> signUp(SignUpRequest request) {
        validateSignupRequest(request);
        UsersTable users = buildNewUserModel(request);
        assignRolesToNewUser(users);
        //sendSignupOTP(users, otpAndTime);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse<>(true,
                        HttpStatus.CREATED.value(),
                        HttpStatus.CREATED,
                        ACCOUNT_CREATED_SUCCESSFULLY));
    }

    private UsersTable buildNewUserModel(SignUpRequest request) {
        return UsersTable.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .userType(Objects.nonNull(request.getUserType()) ? request.getUserType() : UserType.USER)
                .verified(true)
                .isOnline(false)
                .userRoles(null)
                .build();
    }

    private void assignRolesToNewUser(UsersTable users) {
        Set<String> strRoles = users.getUserType().label.equalsIgnoreCase(UserType.USER.label) ?
                Set.of(DefaultRoles.ROLE_USER.label) :
                Set.of(DefaultRoles.ROLE_USER.label, DefaultRoles.ROLE_ADMIN.label);

        Set<Role> roles = new HashSet<>();
        List<String> errorMessages = setUserRoles(strRoles, roles);
        if (!errorMessages.isEmpty()) {
            throw new BadRequestException(errorMessages.get(0));
        }
        users = userRepository.save(users);
        UsersTable roleAssignedUsers = users;
        List<UserRole> userRoleList = roles.stream()
                .map(role -> {
                    UserRole userRole = new UserRole();
                    userRole.setRoleid(role);
                    userRole.setUserrole(roleAssignedUsers);
                    userRole.setUuid(UUID.randomUUID().toString());
                    return userRole;
                })
                .peek(userRoleRepository::save)
                .collect(Collectors.toList());
        roleAssignedUsers.setUserRoles(userRoleList);
        userRepository.save(roleAssignedUsers);
    }

    private void validateSignupRequest(SignUpRequest request) {
        if (Objects.isNull(request)) {
            throw new BadRequestException(INVALID_REQUEST_PARAMETERS);
        }
        if (!Utils.isEmailValid(request.getEmail())) {
            throw new BadRequestException(INVALID_EMAIL);
        }
        if (Objects.nonNull(request.getUserType()) && !Utils.isValidUserType(request.getUserType())) {
            throw new BadRequestException(INVALID_USER_TYPE);
        }
        Optional<UsersTable> userOptional = userRepository.findUserByEmail(request.getEmail());
        if (userOptional.isPresent()) {
            throw new DuplicateRecordException(EMAIL_ALREADY_TAKEN);
        }
    }

    public List<String> setUserRoles(Set<String> strRoles, Set<Role> roles) {
        List<String> errorMessages = new ArrayList<>();
        strRoles.forEach(role -> {
            Optional<Role> optional = roleRepository.findRoleByName(role);
            if (Objects.isNull(optional) || optional.isEmpty()) {
                String errorMessage = String.format(ROLE_NOT_FOUND, role);
                errorMessages.add(errorMessage);
            } else {
                roles.add(optional.get());
            }
        });
        return errorMessages;
    }
}
