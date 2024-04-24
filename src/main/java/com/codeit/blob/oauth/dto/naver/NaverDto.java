package com.codeit.blob.oauth.dto.naver;

import lombok.Getter;

@Getter
// TODO KDY Json Property 설정 필요
public class NaverDto {
    private final String access_token;
    private final String refresh_token;
    private final String token_type;
    private final String expires_in;

    public NaverDto(String access_token, String refresh_token, String token_type, String expires_in) {
        this.access_token = access_token;
        this.refresh_token = refresh_token;
        this.token_type = token_type;
        this.expires_in = expires_in;
    }
}
