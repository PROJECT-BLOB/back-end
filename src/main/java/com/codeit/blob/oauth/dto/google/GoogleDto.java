package com.codeit.blob.oauth.dto.google;

import lombok.Getter;

@Getter
// TODO KDY Json Property 설정 필요
public class GoogleDto {
    private final String access_token;
    private final String expires_in;
    private final String scope;
    private final String token_type;
    private final String id_token;

    public GoogleDto(String access_token, String expires_in, String scope, String token_type, String id_token) {
        this.access_token = access_token;
        this.expires_in = expires_in;
        this.scope = scope;
        this.token_type = token_type;
        this.id_token = id_token;
    }
}