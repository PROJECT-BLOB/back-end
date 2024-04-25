package com.codeit.blob.oauth.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Schema(name = "Oauth 로그인 성공 응답 데이터")
public class OauthResponse {
    private final String oauthId;
    private final String accessToken;
    private final String refreshToken;

    @Builder
    public OauthResponse(String oauthId, String accessToken, String refreshToken) {
        this.oauthId = oauthId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
