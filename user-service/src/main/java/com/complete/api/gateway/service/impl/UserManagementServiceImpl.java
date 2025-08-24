package com.complete.api.gateway.service.impl;


import com.complete.api.gateway.dto.response.ApiResponse;
import com.complete.api.gateway.enums.UserType;
import com.complete.api.gateway.exceptions.BadRequestException;
import com.complete.api.gateway.exceptions.RecordNotFoundException;
import com.complete.api.gateway.model.UsersTable;
import com.complete.api.gateway.repository.UserRepository;
import com.complete.api.gateway.repository.UserRoleRepository;
import com.complete.api.gateway.service.UserManagementService;
import com.complete.api.gateway.util.Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.complete.api.gateway.util.AppMessages.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserManagementServiceImpl implements UserManagementService {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    @Override
    public ResponseEntity<ApiResponse> getAllUsers(Integer page, Integer size) {
        Page<UsersTable> list = userRepository.findAll(PageRequest.of((Objects.isNull(page) ? 0 : page), (Objects.isNull(size) ? 50 : size)));
        if (list.isEmpty() || Objects.isNull(list)) {
            throw new RecordNotFoundException(NO_USER_FOUND);
        }
        log.info("####### FETCHING ALL USERS...");
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(true,
                        HttpStatus.OK.value(),
                        HttpStatus.OK,
                        USERS_RETRIEVED_SUCCESSFULLY,
                        list.stream()
                                .map(Mapper::mapUserProfileResponse)
                                .filter(Objects::nonNull)
                                .collect(Collectors.toList())
                ));
    }

    @Override
    public ResponseEntity<ApiResponse> filterUsers(Integer page, Integer size, UserType userType) {
        Page<UsersTable> list = userRepository.findUsersByType(
                userType,
                PageRequest.of((Objects.isNull(page) ? 0 : page), (Objects.isNull(size) ? 50 : size)));
        if (list.isEmpty() || Objects.isNull(list)) {
            throw new RecordNotFoundException(NO_USER_FOUND);
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(true,
                        HttpStatus.OK.value(),
                        HttpStatus.OK,
                        USERS_RETRIEVED_SUCCESSFULLY,
                        list.stream()
                                .map(Mapper::mapUserProfileResponse)
                                .filter(Objects::nonNull)
                                .collect(Collectors.toList())
                ));
    }

    @Override
    public ResponseEntity<ApiResponse> searchUsers(String keyword) {
        if (Objects.isNull(keyword)) {
            throw new BadRequestException(NULL_KEYWORD_PARAM);
        }
        List<UsersTable> list = userRepository.searchUsers(keyword);
        if (list.isEmpty() || Objects.isNull(list)) {
            throw new RecordNotFoundException(NO_USER_FOUND);
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(true,
                        HttpStatus.OK.value(),
                        HttpStatus.OK,
                        USERS_RETRIEVED_SUCCESSFULLY,
                        list.stream()
                                .map(Mapper::mapUserProfileResponse)
                                .filter(Objects::nonNull)
                                .collect(Collectors.toList())
                ));
    }

    @Override
    public ResponseEntity<ApiResponse> deleteUser(String uuid) {
        if (Objects.isNull(uuid)) {
            throw new BadRequestException(NULL_UUID_PARAM);
        }
        Optional<UsersTable> optional = userRepository.findUserByUUID(uuid);
        if (optional.isEmpty()) {
            throw new RecordNotFoundException(NO_USER_FOUND_WITH_UUID);
        }
        userRoleRepository.deleteUserRoleByUserId(optional.get().getId());
        userRepository.deleteById(optional.get().getId());
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(true,
                        HttpStatus.OK.value(),
                        HttpStatus.OK,
                        USERS_DELETED_SUCCESSFULLY
                ));
    }
}
