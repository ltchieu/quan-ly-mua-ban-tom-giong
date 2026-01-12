package com.example.quanlytom.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiErrorResponse> handleAppException(AppException ex) {
        ErrorCode code = ex.getErrorCode();
        String messageKey = (ex.getMessage() != null && !ex.getMessage().isBlank()) ? ex.getMessage() : code.getMessageKey();
        String message = resolve(messageKey);

        return build(code.getHttpStatus(), code.getCode(), message);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        FieldError fe = ex.getBindingResult().getFieldErrors().stream().findFirst().orElse(null);
        String messageKey = (fe != null ? fe.getDefaultMessage() : "INVALID_REQUEST");
        String message = resolve(messageKey);

        return build(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), message);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleConstraintViolation(ConstraintViolationException ex) {
        log.warn("Constraint violation: ", ex);
        String message = resolve(ErrorCode.INVALID_REQUEST.getMessageKey());
        return build(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_REQUEST.getCode(), message);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiErrorResponse> handleAuthentication(AuthenticationException ex) {
        log.error("Authentication error: ", ex);
        String message = resolve(ErrorCode.AUTHENTICATION_REQUIRED.getMessageKey());
        return build(HttpStatus.UNAUTHORIZED, ErrorCode.AUTHENTICATION_REQUIRED.getCode(), message);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleAccessDenied(AccessDeniedException ex) {
        log.error("Access denied: ", ex);
        String message = resolve(ErrorCode.ACCESS_DENIED.getMessageKey());
        return build(HttpStatus.FORBIDDEN, ErrorCode.ACCESS_DENIED.getCode(), message);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUnknown(Exception ex) {
        log.error("Unknown error: ", ex);
        String message = resolve(ErrorCode.INTERNAL_SERVER_ERROR.getMessageKey());
        // For security, maybe log the real exception `ex` but return generic message
        return build(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SERVER_ERROR.getCode(), message);
    }

    private String resolve(String key) {
        try {
            return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
        } catch (Exception e) {
            return key;
        }
    }

    private ResponseEntity<ApiErrorResponse> build(HttpStatus httpStatus, int code, String message) {
        return ResponseEntity.status(httpStatus)
                .body(ApiErrorResponse.builder()
                        .code(code)
                        .message(message)
                        .build());
    }
}
