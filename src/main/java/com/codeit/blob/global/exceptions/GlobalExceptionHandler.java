package com.codeit.blob.global.exceptions;

import com.codeit.blob.global.domain.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex) {
        // 400 BAD_REQUEST
        return ResponseEntity.status(400).body(new ErrorResponse(400, "파일이 너무 큽니다. (파일 당 최대 5MB)"));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        // 400 BAD_REQUEST
        return ResponseEntity.status(400).body(new ErrorResponse(400, ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        // 500 INTERNAL_SERVER_ERROR
        return ResponseEntity.status(500).body(new ErrorResponse(500, ex.getMessage()));
    }

}
