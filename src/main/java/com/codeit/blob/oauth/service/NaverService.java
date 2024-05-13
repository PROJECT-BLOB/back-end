package com.codeit.blob.oauth.service;

import com.codeit.blob.oauth.OauthType;
import com.codeit.blob.jwt.provider.JwtProvider;
import com.codeit.blob.oauth.provider.NaverProperties;
import com.codeit.blob.oauth.response.OauthResponse;
import com.codeit.blob.user.UserState;
import com.codeit.blob.user.domain.Users;
import com.codeit.blob.oauth.dto.naver.NaverDto;
import com.codeit.blob.oauth.dto.naver.NaverUserDto;
import com.codeit.blob.user.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NaverService implements OauthService {

    private final NaverProperties properties;
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    @Override
    public OauthType getOauthType() {
        return properties.getOauthType();
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
    public OauthResponse createToken(String code) {
        NaverDto oauthToken = getOauthToken(code);
        NaverUserDto userInfo = getUserInfo(oauthToken.getAccessToken());

        Map<String, Object> extractClaims = new HashMap<>();
        extractClaims.put("oauthId", userInfo.getId());

        String accessToken = jwtProvider.generateAccessToken(extractClaims);
        String refreshToken = jwtProvider.generateRefreshToken(extractClaims);

        Users users = userRepository.findByOauthId(userInfo.getId())
                .orElseGet(() ->
                        Users.builder()
                                .oauthId(userInfo.getId())
                                .email(userInfo.getEmail())
                                .profileUrl(userInfo.getProfile())
                                .state(UserState.INCOMPLETE)
                                .oauthType(properties.getOauthType())
                                .build()
                );
        
        users.setRefreshToken(refreshToken);
        users = userRepository.save(users);

        return OauthResponse.builder()
                .user(users)
                .state(users.getState())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
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
