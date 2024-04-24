package com.codeit.blob.jwt.exception;

public class UserNotValidationException extends RuntimeException {
    private final JwtStatus status;

    public UserNotValidationException() {
        this.status = JwtStatus.JWT_EXPIRED;
    }
}
