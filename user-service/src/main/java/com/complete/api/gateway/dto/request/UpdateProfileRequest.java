package com.complete.api.gateway.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

import static com.complete.api.gateway.util.AppMessages.*;

@Data
public class UpdateProfileRequest implements Serializable {
    @NotNull(message = NULL_NAME_PARAM)
    @NotEmpty(message = EMPTY_NAME_PARAM)
    @Size(max = 100, message = MAX_NAME_LIMIT_EXCEEDED)
    private String name;
}
