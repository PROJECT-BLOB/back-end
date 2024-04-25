package com.codeit.blob.global.exceptions;

import com.codeit.blob.global.domain.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {
        return ResponseEntity.status(ex.getErrorCode().getStatus()).body(new ErrorResponse(ex.getErrorCode()));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex) {
        // 400 BAD_REQUEST
        return ResponseEntity.status(400).body(new ErrorResponse(ErrorCode.IMG_TOO_LARGE));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        // 400 BAD_REQUEST
        return ResponseEntity.status(400).body(new ErrorResponse(400, "BAD_REQUEST", ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        // 500 INTERNAL_SERVER_ERROR
        return ResponseEntity.status(500).body(new ErrorResponse(500, "INTERNAL_SERVER_ERROR", ex.getMessage()));
    }

}
