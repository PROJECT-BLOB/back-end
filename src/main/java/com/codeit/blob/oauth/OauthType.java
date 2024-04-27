package com.codeit.blob.oauth;

import com.codeit.blob.global.exceptions.CustomException;
import com.codeit.blob.global.exceptions.ErrorCode;

import java.util.Arrays;

public enum OauthType {
    GOOGLE,
    KAKAO,
    NAVER;

    public static OauthType toOauthType(String oauthType) {
        return Arrays.stream(OauthType.values())
                .filter(type -> type.name().equals(oauthType.toUpperCase()))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.BAD_ENUM_REQUEST));
    }
}
