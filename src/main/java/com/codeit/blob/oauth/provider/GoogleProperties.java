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
public class GoogleProperties implements OauthProperties {
    @Value("${oauth2.google.client-id}")
    private String clientId;

    @Value("${oauth2.google.secret-key}")
    private String secretKey;

    @Value("${oauth2.google.redirect-url}")
    private String redirectUrl;

    private static final String AUTH_URL = "https://accounts.google.com/o/oauth2/v2/auth";
    private static final String TOKEN_URL = "https://oauth2.googleapis.com/token";
    private static final String USER_INFO_URL = "https://www.googleapis.com/oauth2/v1/userinfo";
    private static final String SCOPE = "profile email";

    private static final OauthType OAUTH_TYPE = OauthType.GOOGLE;

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
