package com.codeit.blob.jwt.exception;

import lombok.Getter;

@Getter
public enum JwtStatus {
    ILLEGAL_ARGUMENT_ERROR(400, "존재하지 않는 회원입니다."),
    JWT_EXPIRED(401, "Jwt 토큰이 만료되었습니다,"),
    JWT_VALIDATED_FAIL(402, "유효하지 않은 토큰입니다."),
    USER_NOT_VALIDATED(405, "인증되지 않은 회원입니다.");

    private final int status;
    private final String message;

    JwtStatus(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
