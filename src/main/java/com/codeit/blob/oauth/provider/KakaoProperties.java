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
public class KakaoProperties implements OauthProperties {
    @Value("${oauth2.kakao.client-id}")
    private String clientId;

    @Value("${oauth2.kakao.secret-key}")
    private String secretKey;

    @Value("${oauth2.kakao.redirect-url}")
    private String redirectUrl;

    private static final String AUTH_URL = "https://kauth.kakao.com/oauth/authorize";
    private static final String TOKEN_URL = "https://kauth.kakao.com/oauth/token";
    private static final String USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";
    private static final String SCOPE = "profile_image account_email";

    private static final OauthType OAUTH_TYPE = OauthType.KAKAO;

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
