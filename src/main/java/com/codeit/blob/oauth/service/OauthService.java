package com.codeit.blob.oauth.service;

import com.codeit.blob.oauth.OauthType;
import com.codeit.blob.oauth.response.OauthResponse;

public interface OauthService {

    OauthType getOauthType();

    String createLoginUrl();

    OauthResponse createToken(String code);

    Object getOauthToken(String code);

    Object getUserInfo(String oauthToken);
}
