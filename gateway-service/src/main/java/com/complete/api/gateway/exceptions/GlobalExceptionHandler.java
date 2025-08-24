package com.complete.api.gateway.exceptions;


import com.complete.api.gateway.dto.ApiResponseDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private Boolean SUCCESS = true;
    private Boolean FAILED = false;

    @ExceptionHandler
    public Mono<ResponseEntity<Object>> handleAnyException(Throwable ex, ServerWebExchange request) {
        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
                body(new ApiResponseDto<>(FAILED, HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        String.join(",", ex.getMessage()))));
    }

    //@Override
    protected Mono<ResponseEntity<Object>> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            ServerWebExchange request) {
        List<String> errors = new ArrayList<String>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }
        return Mono.just(ResponseEntity.status(status).body(new ApiResponseDto<>(FAILED, status.value(), String.join(",", errors))));
    }

    @ExceptionHandler(RecordNotFoundException.class)
    public Mono<ResponseEntity<Object>> handleRecordNotFoundExceptions(RecordNotFoundException exception, ServerWebExchange ServerWebExchange) {
        String requestUrl = ServerWebExchange.getRequest().getPath().contextPath().value();
        //log.warn("{} access through {}", exception.getMessage(), requestUrl);
        exception.printStackTrace();
        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponseDto<>(FAILED, HttpStatus.NOT_FOUND.value(), exception.getMessage())));
    }

    @ExceptionHandler(BadRequestException.class)
    public Mono<ResponseEntity<Object>> handleBadRequestExceptions(BadRequestException exception, ServerWebExchange ServerWebExchange) {
        String requestUrl = ServerWebExchange.getRequest().getPath().contextPath().value();
        //log.warn(" {} access through {}", exception.getMessage(), requestUrl);
        exception.printStackTrace();
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponseDto<>(FAILED, HttpStatus.BAD_REQUEST.value(), exception.getMessage())));
    }

    @ExceptionHandler(TooManyRequestException.class)
    public Mono<ResponseEntity<Object>> handleTooManyRequestException(TooManyRequestException exception, ServerWebExchange ServerWebExchange) {
        String requestUrl = ServerWebExchange.getRequest().getPath().contextPath().value();
        //log.warn(" {} access through {}", exception.getMessage(), requestUrl);
        exception.printStackTrace();
        return Mono.just(ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(new ApiResponseDto<>(FAILED, HttpStatus.TOO_MANY_REQUESTS.value(), exception.getMessage())));
    }

/*    @ExceptionHandler(InternalServerException.class)
    public Mono<ResponseEntity<Object>> handleInternalServerError(InternalServerException exception, ServerWebExchange ServerWebExchange) {
        String requestUrl = ServerWebExchange.getRequest().getPath().contextPath().value();
        log.warn(" {} access through {}", exception.getMessage(), requestUrl);
        exception.printStackTrace();
        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponseDto<>(FAILED, HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage())));
    }*/

    @ExceptionHandler(DuplicateRecordException.class)
    public Mono<ResponseEntity<Object>> handleDuplicateRecordExceptions(DuplicateRecordException exception, ServerWebExchange ServerWebExchange) {
        String requestUrl = ServerWebExchange.getRequest().getPath().contextPath().value();
        //log.warn(" {} access through {}", exception.getMessage(), requestUrl);
        exception.printStackTrace();
        return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponseDto<>(FAILED, HttpStatus.CONFLICT.value(), exception.getMessage())));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public Mono<ResponseEntity<Object>> handleUnauthorizedExceptions(UnauthorizedException exception, ServerWebExchange ServerWebExchange) {
        String requestUrl = ServerWebExchange.getRequest().getPath().contextPath().value();
        //log.warn(" {} access through {}", exception.getMessage(), requestUrl);
        exception.printStackTrace();
        return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponseDto<>(FAILED, HttpStatus.UNAUTHORIZED.value(), exception.getMessage())));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public Mono<ResponseEntity<Object>> handleAccessDeniedExceptions(AccessDeniedException exception, ServerWebExchange ServerWebExchange) {
        String requestUrl = ServerWebExchange.getRequest().getPath().contextPath().value();
        //log.warn(" {} access through {}", exception.getMessage(), requestUrl);
        exception.printStackTrace();
        return Mono.just(ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponseDto<>(FAILED, HttpStatus.FORBIDDEN.value(), exception.getMessage())));
    }

    //@ExceptionHandler(IllegalArgumentException.class)
    @ExceptionHandler(InternalServerException.class)
    public Mono<ResponseEntity<Object>> handleIllegalArgumentExceptions(InternalServerException exception, ServerWebExchange ServerWebExchange) {
        String requestUrl = ServerWebExchange.getRequest().getPath().contextPath().value();
        //log.warn(" {} access through {}", exception.getMessage(), requestUrl);
        exception.printStackTrace();
        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponseDto<>(FAILED, HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage())));
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public Mono<ResponseEntity<Object>> handleMethodArgumentTypeMismatchExceptions(MethodArgumentTypeMismatchException exception, ServerWebExchange ServerWebExchange) {
        String requestUrl = ServerWebExchange.getRequest().getPath().contextPath().value();
        String error = exception.getName() + " should be of type " + exception.getRequiredType().getName();
        //log.warn(" {} access through {}", exception.getMessage(), requestUrl);
        exception.printStackTrace();
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponseDto<>(FAILED, HttpStatus.BAD_REQUEST.value(), error)));
    }

    @ExceptionHandler(value = Exception.class)
    public Mono<ResponseEntity<Object>> handlerGlobalErrors(Exception exception) {
        exception.printStackTrace();
        //log.warn("An error occur  {}", exception.fillInStackTrace());
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponseDto<>(FAILED, HttpStatus.BAD_REQUEST.value(), exception.getMessage())));
    }

    @ExceptionHandler(SuccessNotification.class)
    public Mono<ResponseEntity<Object>> handleSuccessNotification(SuccessNotification exception, ServerWebExchange ServerWebExchange) {
        String requestUrl = ServerWebExchange.getRequest().getPath().contextPath().value();
        //log.warn(" {} access through {}", exception.getMessage(), requestUrl);
        exception.printStackTrace();
        return Mono.just(ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto<>(SUCCESS, HttpStatus.OK.value(), exception.getMessage())));
    }
}
