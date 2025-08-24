package com.complete.api.gateway.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> implements Serializable {
    private Boolean success;
    private Integer code;
    private HttpStatus status;
    private String message;
    private T data = (T) new ArrayList<>();
    private Map<String, Object> meta = new HashMap<>();

    public Map<String, Object> getMeta() {
        return meta;
    }

    public ApiResponse addMeta(String key, Object value) {
        meta.put(key, value);
        return this;
    }

    public ApiResponse(Boolean success, Integer code, T data) {
        this.success = success;
        this.code = code;
        this.data = data;
    }

    public ApiResponse(Boolean success, Integer code, HttpStatus status, T data) {
        this.success = success;
        this.code = code;
        this.status = status;
        this.data = data;
    }

    public ApiResponse(Boolean success, Integer code, HttpStatus status, String message) {
        this.success = success;
        this.code = code;
        this.status = status;
        this.message = message;
    }

    public ApiResponse(Boolean success, Integer code, HttpStatus status, String message, T data) {
        this.success = success;
        this.code = code;
        this.status = status;
        this.message = message;
        this.data = data;
    }
}