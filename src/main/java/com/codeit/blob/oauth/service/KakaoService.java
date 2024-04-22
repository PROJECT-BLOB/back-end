package com.codeit.blob.oauth.service;

import com.codeit.blob.account.UserAuthenticateType;
import com.codeit.blob.account.request.AccountRequest;
import com.codeit.blob.oauth.OauthType;
import com.codeit.blob.account.domain.Users;
import com.codeit.blob.oauth.dto.kakao.KakaoDto;
import com.codeit.blob.oauth.dto.kakao.KakaoUserDto;
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

@Service
public class KakaoService extends OauthService {
    private final AccountRepository accountRepository;

    public KakaoService(@Qualifier("kakaoProperties") OauthProperties properties, AccountRepository accountRepository) {
        super(properties);
        this.accountRepository = accountRepository;
    }

    @Override
    public String createLoginUrl() {
        return UriComponentsBuilder.fromHttpUrl(properties.getAuthUrl())
                .queryParam("client_id", properties.getClientId())
                .queryParam("redirect_uri", properties.getRedirectUrl())
                .queryParam("scope", properties.getScope())
                .queryParam("response_type", "code")
                .toUriString();
    }

    @Override
    public Object createToken(String code) {
        KakaoDto oauthToken = getOauthToken(code);
        String accessToken = oauthToken.getAccess_token();
        KakaoUserDto userInfo = getUserInfo(accessToken);

        WebClient.create().post()
                .uri("http://localhost:8080/account")
                .bodyValue(
                        AccountRequest.builder()
                                .oauthId(userInfo.getId())
                                .email(userInfo.getEmail())
                                .profileUrl(userInfo.getProfile())
                                .userAuthenticateType(UserAuthenticateType.BLOCKED)
                                .oauthType(OauthType.KAKAO)
                                .build()
                ).retrieve()
                .bodyToMono(Void.class)
                .subscribe();

        return userInfo;
    }

    @Override
    public KakaoDto getOauthToken(String code) {
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", properties.getClientId());
        params.add("redirect_uri", properties.getRedirectUrl());
        params.add("grant_type", "authorization_code");


        return WebClient.create().post()
                .uri(properties.getTokenUrl())
                .header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                .bodyValue(params)
                .retrieve()
                .bodyToMono(KakaoDto.class)
                .onErrorMap(JsonProcessingException.class, e -> new IllegalArgumentException("Json Parse Error."))
                .onErrorMap(WebClientResponseException.class, e -> new IllegalArgumentException(e.getStatusCode() + " 토큰 정보 가져오는 중 오류 발생"))
                .block();
    }

    @Override
    public KakaoUserDto getUserInfo(String oauthToken) {
        return WebClient.create().get()
                .uri(properties.getUserInfoUrl())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + oauthToken)
                .header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                .retrieve()
                .bodyToMono(KakaoUserDto.class)
                .onErrorMap(JsonProcessingException.class, e -> new IllegalArgumentException("Json Parse Error."))
                .onErrorMap(WebClientResponseException.class, e -> new IllegalArgumentException(e.getStatusCode() + " 유저 정보 가져오는 과정에서 오류 발생"))
                .block();
    }
}
