package com.codeit.blob.oauth.exception;

import com.codeit.blob.jwt.exception.JwtExpiredException;
import com.codeit.blob.jwt.exception.JwtStatus;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.security.SignatureException;

@RestControllerAdvice
public class OauthExceptionHandler {
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorResponse> expiredJwtExceptionHandler(ExpiredJwtException e) {
        return ResponseEntity.status(402).body(new ErrorResponse(JwtStatus.JWT_EXPIRED));
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ErrorResponse> signatureExceptionHandler(SignatureException e) {
        return ResponseEntity.status(402).body(new ErrorResponse(JwtStatus.JWT_VALIDATED_FAIL));
    }

    @Getter
    public static class ErrorResponse {
        private final int status;
        private final String message;

        public ErrorResponse(JwtStatus status) {
            this.status = status.getStatus();
            this.message = status.getMessage();
        }
    }
}
