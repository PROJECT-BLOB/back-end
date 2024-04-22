package com.codeit.blob.oauth.service;

import com.codeit.blob.oauth.OauthType;
import com.codeit.blob.oauth.provider.OauthProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
public abstract class OauthService {
    protected final OauthProperties properties;
    public final OauthType oauthType;

    public OauthService(OauthProperties properties) {
        this.properties = properties;
        this.oauthType = properties.getOauthType();
    }

    public String createLoginUrl() {
        return UriComponentsBuilder.fromHttpUrl(properties.getAuthUrl())
                .queryParam("client_id", properties.getClientId())
                .queryParam("redirect_uri", properties.getRedirectUrl())
                .queryParam("scope", properties.getScope())
                .queryParam("response_type", "code")
                .toUriString();
    }

    public abstract Object createToken(String code);

    public abstract Object getOauthToken(String code);

    public abstract Object getUserInfo(String oauthToken);
}
