package com.codeit.blob.oauth.response;

import lombok.Builder;
import lombok.Getter;

@Getter
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
