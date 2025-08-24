package com.complete.api.gateway.security.user;


import com.complete.api.gateway.model.Role;
import com.complete.api.gateway.model.UsersTable;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
public class UserDetailsImpl implements UserDetails {
    private String name;
    private String userType;
    private String userId;
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> roles;
    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(String name, String userType, String userId, String username, String password,
                           Collection<? extends GrantedAuthority> roles,
                           Collection<? extends GrantedAuthority> authorities) {
        this.name = name;
        this.userId = userId;
        this.userType = userType;
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.authorities = authorities;
    }


    public static UserDetailsImpl build(UsersTable users) {
        return new UserDetailsImpl(
                (users.getName()),
                users.getUserType().label,
                users.getUuid(),
                users.getEmail(),
                users.getPassword(),
                processUserRoles(users),
                processUserAuthorities(users));
    }

    private static List<GrantedAuthority> processUserRoles(UsersTable users) {
        return users.getUserRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleid().getName()))
                .collect(Collectors.toList());
    }

    private static List<GrantedAuthority> processUserAuthorities(UsersTable users) {
        return removeDuplicatesFromAuthorities(
                users.getUserRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(getListOfPermissionsFromRoles(role.getRoleid())))
                        .collect(Collectors.toList())
        );
    }

    public Collection<? extends GrantedAuthority> getRoles() {
        return roles;
    }

    public static String getListOfPermissionsFromRoles(Role userRole) {
        StringBuilder stringBuilder = new StringBuilder();
        userRole.getPermissions().forEach(permissionTypes -> {
            stringBuilder.append(permissionTypes.label).append(",");
        });
        if (stringBuilder.length() > 0) {
            // Remove the trailing comma
            stringBuilder.setLength(stringBuilder.length() - 1);
        }
        return stringBuilder.toString();
    }

    public static List<String> removeDuplicatesPermissions(List<String> permissions) {
        return List.copyOf(permissions.stream().collect(Collectors.toSet()));
    }

    public static List<GrantedAuthority> removeDuplicatesFromAuthorities(List<GrantedAuthority> permissions) {
        StringBuilder builder = new StringBuilder();
        for (GrantedAuthority authority : permissions) {
            List<String> roleAuthorities = Arrays.stream(authority.getAuthority().split(",")).toList();
            if (Objects.nonNull(roleAuthorities) && !roleAuthorities.isEmpty()) {
                for (String permit : roleAuthorities) {
                    builder.append("ROLE_" + permit).append(",");
                }
            }
        }
        if (builder.length() > 0) {
            // Remove the trailing comma
            builder.setLength(builder.length() - 1);
        }
        List<String> cleanedList = removeDuplicatesPermissions(
                Arrays.stream(builder.toString().split(",")).toList()
        );
        return cleanedList.stream()
                .map(authority -> new SimpleGrantedAuthority(authority))
                .collect(Collectors.toList());
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
