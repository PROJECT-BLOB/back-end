package com.codeit.blob.jwt.exception;

import lombok.Getter;

@Getter

public class JwtExpiredException extends RuntimeException {
    private final JwtStatus status;

    public JwtExpiredException() {
        this.status = JwtStatus.JWT_EXPIRED;
    }
}
