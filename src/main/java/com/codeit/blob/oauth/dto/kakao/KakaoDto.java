package com.codeit.blob.oauth.dto.kakao;

import lombok.Getter;

@Getter
// TODO KDY Json Property 설정 필요
public class KakaoDto {
    private final String access_token;
    private final String token_type;
    private final String refresh_token;
    private final String expires_in;
    private final String scope;
    private final String refresh_token_expires_in;

    public KakaoDto(String access_token, String token_type, String refresh_token, String expires_in, String scope, String refresh_token_expires_in) {
        this.access_token = access_token;
        this.token_type = token_type;
        this.refresh_token = refresh_token;
        this.expires_in = expires_in;
        this.scope = scope;
        this.refresh_token_expires_in = refresh_token_expires_in;
    }
}
