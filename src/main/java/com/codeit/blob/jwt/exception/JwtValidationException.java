package com.codeit.blob.jwt.exception;

import lombok.Getter;

@Getter
public class JwtValidationException extends RuntimeException {
    private final JwtStatus status;

    public JwtValidationException() {
        this.status = JwtStatus.JWT_VALIDATED_FAIL;
    }
}
