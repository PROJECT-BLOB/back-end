package com.codeit.blob.oauth.service;

import com.codeit.blob.oauth.OauthType;
import com.codeit.blob.oauth.response.OauthResponse;

public interface OauthService {

    String URL = "http://localhost:3000/oauth/";

    OauthType getOauthType();

    String createLoginUrl();

    String createLocalLoginUrl();

    OauthResponse createToken(String code, boolean isLocal);

    Object getOauthToken(String code, boolean isLocal);

    Object getUserInfo(String oauthToken);
}
