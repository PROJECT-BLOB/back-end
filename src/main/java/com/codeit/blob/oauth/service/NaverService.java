package com.codeit.blob.oauth.service;

import com.codeit.blob.account.UserAuthenticateType;
import com.codeit.blob.account.request.AccountRequest;
import com.codeit.blob.oauth.OauthType;
import com.codeit.blob.oauth.dto.naver.NaverDto;
import com.codeit.blob.oauth.dto.naver.NaverUserDto;
import com.codeit.blob.oauth.provider.OauthProperties;
import com.codeit.blob.account.repository.AccountRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class NaverService extends OauthService {
    private final AccountRepository accountRepository;

    public NaverService(@Qualifier("naverProperties") OauthProperties properties, AccountRepository accountRepository) {
        super(properties);
        this.accountRepository = accountRepository;
    }

    @Override
    public String createLoginUrl() {
        return UriComponentsBuilder.fromHttpUrl(properties.getAuthUrl())
                .queryParam("client_id", properties.getClientId())
                .queryParam("redirect_uri", properties.getRedirectUrl())
                .queryParam("response_type", "code")
                .queryParam("state", URLEncoder.encode("blob", StandardCharsets.UTF_8))
                .toUriString();
    }

    @Override
    public Object createToken(String code) {
        NaverDto oauthToken = getOauthToken(code);
        NaverUserDto userInfo = getUserInfo(oauthToken.getAccess_token());

        WebClient.create().post()
                .uri("http://localhost:8080/account")
                .bodyValue(
                        AccountRequest.builder()
                                .oauthId(userInfo.getId())
                                .email(userInfo.getEmail())
                                .profileUrl(userInfo.getProfile())
                                .userAuthenticateType(UserAuthenticateType.BLOCKED)
                                .oauthType(OauthType.NAVER)
                                .build()
                ).retrieve()
                .bodyToMono(Void.class)
                .subscribe();

        return userInfo;
    }

    @Override
    public NaverDto getOauthToken(String code) {
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", properties.getClientId());
        params.add("client_secret", properties.getSecretKey());
        params.add("redirect_uri", properties.getRedirectUrl());
        params.add("grant_type", "authorization_code");
        params.add("state", URLEncoder.encode("blob", StandardCharsets.UTF_8));

        return WebClient.create().post()
                .uri(properties.getTokenUrl())
                .header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                .bodyValue(params)
                .retrieve()
                .bodyToMono(NaverDto.class)
                .onErrorMap(JsonProcessingException.class, e -> new IllegalArgumentException("Json Parse Error."))
                .onErrorMap(WebClientResponseException.class, e -> new IllegalArgumentException(e.getStatusCode() + " 토큰 정보 가져오는 중 오류 발생"))
                .block();
    }

    @Override
    public NaverUserDto getUserInfo(String oauthToken) {
        return WebClient.create().get()
                .uri(properties.getUserInfoUrl())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + oauthToken)
                .header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                .retrieve()
                .bodyToMono(NaverUserDto.class)
                .onErrorMap(JsonProcessingException.class, e -> new IllegalArgumentException("Json Parse Error."))
                .onErrorMap(WebClientResponseException.class, e -> new IllegalArgumentException(e.getStatusCode() + " 유저 정보 가져오는 과정에서 오류 발생"))
                .block();
    }
}
