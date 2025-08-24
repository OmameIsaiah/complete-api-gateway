package com.complete.api.gateway.model.listener;


import com.complete.api.gateway.model.Role;
import com.complete.api.gateway.model.UserRole;
import com.complete.api.gateway.model.UsersTable;
import com.complete.api.gateway.repository.UserRepository;
import com.complete.api.gateway.security.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.persistence.*;

import java.util.UUID;

@Component
@Slf4j
public class EntityListener {
    private static UserRepository userRepository;

    @Autowired
    public void setAccountsRepo(UserRepository userRepository) {
        EntityListener.userRepository = userRepository;
    }

    @PrePersist
    private void beforeCreate(Object data) {
        if (data instanceof UsersTable) {
            UsersTable users = (UsersTable) data;
            users.setUuid(UUID.randomUUID().toString());
        } else if (data instanceof Role) {
            Role role = (Role) data;
            role.setUuid(UUID.randomUUID().toString());
        } else if (data instanceof UserRole) {
            UserRole userRole = (UserRole) data;
            userRole.setUuid(UUID.randomUUID().toString());
        }
    }

    @PostPersist
    private void afterCreate(Object data) {
        if (data instanceof UsersTable) {
            UsersTable users = (UsersTable) data;
            users.setUserToken(SecurityUtils.encode(users.getEmail() + users.getId()));
            userRepository.save(users);
        }
    }
}
