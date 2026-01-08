package com.example.quanlytom.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    // --- common ---
    INTERNAL_SERVER_ERROR(5000, HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi hệ thống"),
    INVALID_REQUEST(4000, HttpStatus.BAD_REQUEST, "Dữ liệu không hợp lệ"),

    // Generic unauthorized
    UNAUTHORIZED(4010, HttpStatus.UNAUTHORIZED, "Unauthorized"),

    // --- user ---
    USER_NOT_FOUND(4041, HttpStatus.NOT_FOUND, "Không tìm thấy người dùng"),

    // --- signup ---
    USER_PHONE_OR_EMAIL_EXIST(1005, HttpStatus.BAD_REQUEST, "Số điện thoại hoặc email đã tồn tại"),

    // --- authentication (401) ---
    AUTHENTICATION_REQUIRED(4011, HttpStatus.UNAUTHORIZED, "Vui lòng đăng nhập"),
    INVALID_CREDENTIALS(4012, HttpStatus.UNAUTHORIZED, "Sai thông tin đăng nhập"),

    // Generic token errors
    TOKEN_INVALID(4013, HttpStatus.UNAUTHORIZED, "Token không hợp lệ"),
    TOKEN_EXPIRED(4014, HttpStatus.UNAUTHORIZED, "Token đã hết hạn"),

    // JWT specific errors (used in catch blocks)
    INVALID_JWT_TOKEN(4015, HttpStatus.UNAUTHORIZED, "JWT token không hợp lệ"),
    EXPIRED_JWT_TOKEN(4016, HttpStatus.UNAUTHORIZED, "JWT token đã hết hạn"),
    UNSUPPORT_TOKEN(4017, HttpStatus.UNAUTHORIZED, "JWT token không được hỗ trợ"),
    JWT_CLAIMS_EMPTY(4018, HttpStatus.UNAUTHORIZED, "JWT claims rỗng"),

    // --- refresh token (401) ---
    REFRESH_TOKEN_NOT_FOUND(4019, HttpStatus.UNAUTHORIZED, "Không tìm thấy refresh token"),
    REFRESH_TOKEN_EXPIRED(4020, HttpStatus.UNAUTHORIZED, "Refresh token đã hết hạn"),
    REFRESH_TOKEN_REVOKED(4021, HttpStatus.UNAUTHORIZED, "Refresh token đã bị thu hồi"),

    // --- authorization (403) ---
    ACCESS_DENIED(4031, HttpStatus.FORBIDDEN, "Bạn không có quyền truy cập"),
    ROLE_NOT_ALLOWED(4032, HttpStatus.FORBIDDEN, "Vai trò không được phép thực hiện hành động này");

    private final Integer code;
    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(Integer code, HttpStatus httpStatus, String message) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getMessage() {
        return message;
    }
}
