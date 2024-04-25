package com.codeit.blob.oauth.exception;

import com.codeit.blob.global.domain.ErrorResponse;
import com.codeit.blob.jwt.exception.JwtStatus;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.security.SignatureException;

@RestControllerAdvice
public class OauthExceptionHandler {
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorResponse> expiredJwtExceptionHandler(ExpiredJwtException e) {
        JwtStatus status = JwtStatus.JWT_EXPIRED;
        return ResponseEntity.status(402).body(new ErrorResponse(status.getStatus(), status.name(), status.getMessage()));
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ErrorResponse> signatureExceptionHandler(SignatureException e) {
        JwtStatus status = JwtStatus.JWT_VALIDATED_FAIL;
        return ResponseEntity.status(402).body(new ErrorResponse(status.getStatus(), status.name(), status.getMessage()));
    }
}
