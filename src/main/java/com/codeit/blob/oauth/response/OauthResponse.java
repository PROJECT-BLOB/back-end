package com.codeit.blob.oauth.response;

import lombok.Getter;

@Getter
public class OauthResponse {
    private final String oauthId;

    public OauthResponse(String oauthId) {
        this.oauthId = oauthId;
    }
}
