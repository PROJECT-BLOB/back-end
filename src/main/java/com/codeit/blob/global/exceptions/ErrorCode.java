package com.codeit.blob.global.exceptions;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // 400 BAD_REQUEST
    IMG_TOO_LARGE(400, "사진이 너무 큽니다. (파일 당 최대 5MB)"),
    UNSUPPORTED_MEDIA_TYPE(400, "잘못된 형식의 파일입니다. (JPG, JPEG, PNG만 가능)"),

    //403 FORBIDDEN
    ACTION_ACCESS_DENIED(403, "이 작업을 수행할 수 있는 권한이 없습니다."),
    LOGIN_REQUIRED(403, "이 작업을 수행하려면 로그인하세요."),

    //404 NOT_FOUND
    USER_NOT_FOUND(404, "유저를 찾을 수 없습니다."),
    POST_NOT_FOUND(404, "게시글을 찾을 수 없습니다."),
    COMMENT_NOT_FOUND(404,"댓글을 찾을수 없습니다."),

    //500 INTERNAL SERVER ERROR
    IMG_UPLOAD_FAIL(500, "사진 업로드에 실패하였습니다.");

    private final int status;
    private final String message;

    ErrorCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}