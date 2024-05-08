package com.codeit.blob.global.exceptions;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // 400 BAD_REQUEST
    IMG_TOO_LARGE(400, "사진이 너무 큽니다. (파일 당 최대 5MB)"),
    UNSUPPORTED_MEDIA_TYPE(400, "잘못된 형식의 파일입니다. (JPG, JPEG, PNG만 가능)"),
    BAD_ENUM_REQUEST(400, "잘못된 형식의 enum 입니다."),
    DUPLICATE_BLOB_ID(400, "중복된 아이디 입니다."),
    BAD_BIO_LENGTH(400, "BIO 는 최대 50글자 입니다."),

    // 401 UNAUTHORIZED
    JWT_EXPIRED(401, "jwt 토큰이 만료되었습니다,"),
    JWT_VALIDATED_FAIL(401, "유효하지 않은 토큰입니다."),

    //403 FORBIDDEN
    ACTION_ACCESS_DENIED(403, "이 작업을 수행할 수 있는 권한이 없습니다."),
    PRIVATE_PROFILE(403, "비공개 프로필입니다."),
    LOGIN_REQUIRED(403, "이 작업을 수행하려면 로그인하세요."),

    //404 NOT_FOUND
    USER_NOT_FOUND(404, "유저를 찾을 수 없습니다."),
    POST_NOT_FOUND(404, "게시글을 찾을 수 없습니다."),
    COMMENT_NOT_FOUND(404, "댓글을 찾을 수 없습니다."),
    NOTIFICATION_NOT_FOUND(404, "알림을 찾을 수 없습니다."),
    COUNTRY_NOT_FOUND(404, "나라를 찾을 수 없습니다."),
    CATEGORY_NOT_FOUND(404, "카테고리를 찾을 수 없습니다."),
    SUBCATEGORY_NOT_FOUND(404, "세부 카테고리를 찾을 수 없습니다."),

    //405 403으로 수정?
    NEED_MORE_AUTHENTICATE(405, "추가적인 인증이 필요합니다."),

    //409 CONFLICT
    REPORT_ALREADY_EXISTS(409, "이미 신고한 게시글/댓글입니다."),

    //500 INTERNAL SERVER ERROR
    IMG_UPLOAD_FAIL(500, "사진 업로드에 실패하였습니다."),
    IMG_DELETE_FAIL(500, "사진 삭제에 실패하였습니다.");

    private final int status;
    private final String message;

    ErrorCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
