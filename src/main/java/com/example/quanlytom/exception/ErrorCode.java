package com.example.quanlytom.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    // --- common ---
    INTERNAL_SERVER_ERROR(5000, HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR"),
    INVALID_REQUEST(4000, HttpStatus.BAD_REQUEST, "INVALID_REQUEST"),

    // Generic unauthorized
    UNAUTHORIZED(4010, HttpStatus.UNAUTHORIZED, "UNAUTHORIZED"),

    // --- user ---
    USER_NOT_FOUND(4041, HttpStatus.NOT_FOUND, "USER_NOT_FOUND"),

    // --- signup ---
    USER_PHONE_OR_EMAIL_EXIST(1005, HttpStatus.BAD_REQUEST, "USER_PHONE_OR_EMAIL_EXIST"),

    // --- authentication (401) ---
    AUTHENTICATION_REQUIRED(4011, HttpStatus.UNAUTHORIZED, "AUTHENTICATION_REQUIRED"),
    INVALID_CREDENTIALS(4012, HttpStatus.UNAUTHORIZED, "INVALID_CREDENTIALS"),

    // Generic token errors
    TOKEN_INVALID(4013, HttpStatus.UNAUTHORIZED, "TOKEN_INVALID"),
    TOKEN_EXPIRED(4014, HttpStatus.UNAUTHORIZED, "TOKEN_EXPIRED"),

    // JWT specific errors (used in catch blocks)
    INVALID_JWT_TOKEN(4015, HttpStatus.UNAUTHORIZED, "INVALID_JWT_TOKEN"),
    EXPIRED_JWT_TOKEN(4016, HttpStatus.UNAUTHORIZED, "EXPIRED_JWT_TOKEN"),
    UNSUPPORT_TOKEN(4017, HttpStatus.UNAUTHORIZED, "UNSUPPORT_TOKEN"),
    JWT_CLAIMS_EMPTY(4018, HttpStatus.UNAUTHORIZED, "JWT_CLAIMS_EMPTY"),

    // --- refresh token (401) ---
    REFRESH_TOKEN_NOT_FOUND(4019, HttpStatus.UNAUTHORIZED, "REFRESH_TOKEN_NOT_FOUND"),
    REFRESH_TOKEN_EXPIRED(4020, HttpStatus.UNAUTHORIZED, "REFRESH_TOKEN_EXPIRED"),
    REFRESH_TOKEN_REVOKED(4021, HttpStatus.UNAUTHORIZED, "REFRESH_TOKEN_REVOKED"),

    // --- authorization (403) ---
    ACCESS_DENIED(4031, HttpStatus.FORBIDDEN, "ACCESS_DENIED"),
    ROLE_NOT_ALLOWED(4032, HttpStatus.FORBIDDEN, "ROLE_NOT_ALLOWED");

    private final Integer code;
    private final HttpStatus httpStatus;
    /**
     * Message key to be resolved from messages.properties via MessageSource.
     */
    private final String messageKey;

    ErrorCode(Integer code, HttpStatus httpStatus, String messageKey) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.messageKey = messageKey;
    }

    public Integer getCode() {
        return code;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getMessageKey() {
        return messageKey;
    }
}
