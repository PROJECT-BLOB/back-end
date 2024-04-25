package com.codeit.blob.global.domain;

import com.codeit.blob.global.exceptions.ErrorCode;
import lombok.Getter;

@Getter
public class ErrorResponse {
    private final int status;
    private final String name;
    private final String message;

    public ErrorResponse(int status, String name, String message) {
        this.status = status;
        this.name = name;
        this.message = message;
    }

    public ErrorResponse(ErrorCode errorCode){
        this.status = errorCode.getStatus();
        this.name = errorCode.name();
        this.message = errorCode.getMessage();
    }
}
