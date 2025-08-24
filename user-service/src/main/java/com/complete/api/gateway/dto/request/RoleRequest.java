package com.complete.api.gateway.dto.request;

import com.complete.api.gateway.enums.Permissions;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

import static com.complete.api.gateway.util.AppMessages.EMPTY_ROLE_NAME;
import static com.complete.api.gateway.util.AppMessages.NULL_ROLE_NAME;


@Data
public class RoleRequest implements Serializable {
    @NotNull(message = NULL_ROLE_NAME)
    @NotEmpty(message = EMPTY_ROLE_NAME)
    private String roleName;
    private String description;
    private Set<Permissions> permissions;
}
