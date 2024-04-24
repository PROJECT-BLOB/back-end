package com.codeit.blob.oauth;

import java.util.Arrays;

public enum OauthType {
    GOOGLE,
    KAKAO,
    NAVER;

    public static OauthType toOauthType(String oauthType) {
        return Arrays.stream(OauthType.values())
                .filter(type -> type.name().equals(oauthType.toUpperCase()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Oauth Type 입니다. {}" + oauthType));
    }
}
