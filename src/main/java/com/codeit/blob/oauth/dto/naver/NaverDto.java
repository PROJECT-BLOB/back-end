package com.codeit.blob.oauth.dto.naver;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class NaverDto {
    private final String accessToken;
    private final String refreshToken;
    private final String tokenType;
    private final String expiresIn;

    public NaverDto(String accessToken, String refreshToken, String tokenType, String expiresIn) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
    }
}
