package com.codeit.blob.oauth.provider;

import com.codeit.blob.oauth.OauthType;

public interface OauthProperties {

    OauthType getOauthType();

    String getClientId();

    String getRedirectUrl();

    String getAuthUrl();

    String getTokenUrl();

    String getUserInfoUrl();

    String getScope();

    String getSecretKey();
}
