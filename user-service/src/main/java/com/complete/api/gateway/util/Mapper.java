package com.complete.api.gateway.util;


import com.complete.api.gateway.dto.response.RoleResponse;
import com.complete.api.gateway.dto.response.UserProfileResponse;
import com.complete.api.gateway.model.Role;
import com.complete.api.gateway.model.UserRole;
import com.complete.api.gateway.model.UsersTable;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


public class Mapper {
    public static RoleResponse mapRoleToRoleResponse(Role role) {
        return Optional.ofNullable(
                        Objects.isNull(role) ? null :
                                RoleResponse.builder()
                                        .uuid(role.getUuid())
                                        .name(role.getName())
                                        .description(role.getDescription())
                                        .permissions(role.getPermissions())
                                        .build())
                .orElse(null);
    }

    public static UserProfileResponse mapUserProfileResponse(UsersTable users) {
        return Optional.ofNullable(
                        Objects.isNull(users) ? null :
                                UserProfileResponse.builder()
                                        .uuid(users.getUuid())
                                        .name(users.getName())
                                        .email(users.getEmail())
                                        .userType(users.getUserType())
                                        .userToken(users.getUserToken())
                                        .isOnline(users.getIsOnline())
                                        .lastLogin(Utils.convertLocalDateTimeToString(users.getLastLogin()))
                                        .roles(getRoles(users.getUserRoles()))
                                        .permissions(getPermissions(users.getUserRoles()))
                                        .build())
                .orElse(null);
    }

    private static List<String> getRoles(List<UserRole> userRoleList) {
        return userRoleList.stream()
                .filter(Objects::nonNull)
                .map(item -> item.getRoleid().getName())
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private static List<String> getPermissions(List<UserRole> userRoleList) {
        return removeDuplicates(userRoleList.stream()
                .filter(Objects::nonNull)
                .map(item -> getCommaSeparatedPermissionsFromRoles(item.getRoleid()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList()));
    }

    private static String getCommaSeparatedPermissionsFromRoles(Role role) {
        StringBuilder stringBuilder = new StringBuilder();
        role.getPermissions().forEach(permissionTypes -> {
            stringBuilder.append(permissionTypes.label).append(",");
        });
        return stringBuilder.toString();
    }

    private static List<String> removeDuplicates(List<String> permissions) {
        StringBuilder builder = new StringBuilder();
        for (String authority : permissions) {
            List<String> roleAuthorities = Arrays.stream(authority.split(",")).collect(Collectors.toList());
            if (Objects.nonNull(roleAuthorities) && !roleAuthorities.isEmpty()) {
                for (String permit : roleAuthorities) {
                    builder.append(permit).append(",");
                }
            }
        }
        if (builder.length() > 0) {
            builder.setLength(builder.length() - 1);
        }
        return removeDuplicatesPermissions(Arrays.stream(builder.toString().split(",")).collect(Collectors.toList()));
    }

    private static List<String> removeDuplicatesPermissions(List<String> permissions) {
        return List.copyOf(permissions.stream().collect(Collectors.toSet()));
    }
}
