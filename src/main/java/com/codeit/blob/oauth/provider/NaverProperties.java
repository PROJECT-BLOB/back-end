package com.codeit.blob.oauth.provider;

import com.codeit.blob.oauth.OauthType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NaverProperties implements OauthProperties {

    @Value("${oauth2.naver.client-id}")
    private String clientId;

    @Value("${oauth2.naver.secret-key}")
    private String secretKey;

    @Value("${oauth2.naver.redirect-url}")
    private String redirectUrl;

    private static final String AUTH_URL = "https://nid.naver.com/oauth2.0/authorize";
    private static final String TOKEN_URL = "https://nid.naver.com/oauth2.0/token";
    private static final String USER_INFO_URL = "https://openapi.naver.com/v1/nid/me";
    private static final String SCOPE = "";

    private static final OauthType OAUTH_TYPE = OauthType.NAVER;

    @Override
    public OauthType getOauthType() {
        return OAUTH_TYPE;
    }

    @Override
    public String getAuthUrl() {
        return AUTH_URL;
    }

    @Override
    public String getTokenUrl() {
        return TOKEN_URL;
    }

    @Override
    public String getUserInfoUrl() {
        return USER_INFO_URL;
    }

    @Override
    public String getScope() {
        return SCOPE;
    }
}
