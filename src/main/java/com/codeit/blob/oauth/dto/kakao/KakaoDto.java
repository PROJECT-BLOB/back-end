package com.codeit.blob.oauth.dto.kakao;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoDto {
    private final String accessToken;
    private final String tokenType;
    private final String refreshToken;
    private final String expiresIn;
    private final String scope;
    private final String refreshTokenExpiresIn;

    public KakaoDto(String accessToken, String tokenType, String refreshToken, String expiresIn, String scope, String refreshTokenExpiresIn) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
        this.scope = scope;
        this.refreshTokenExpiresIn = refreshTokenExpiresIn;
    }
}
