package com.codeit.blob.oauth.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(name = "Access Token 재발급 응답 데이터")
public class TokenResponse {
    private final String accessToken;

    public TokenResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
