package com.codeit.blob.oauth.service;

import com.codeit.blob.oauth.OauthType;
import com.codeit.blob.jwt.provider.JwtProvider;
import com.codeit.blob.oauth.provider.GoogleProperties;
import com.codeit.blob.oauth.response.OauthResponse;
import com.codeit.blob.user.UserAuthenticateState;
import com.codeit.blob.user.domain.Users;
import com.codeit.blob.user.repository.UserRepository;
import com.codeit.blob.oauth.dto.google.GoogleDto;
import com.codeit.blob.oauth.dto.google.GoogleUserDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GoogleService implements OauthService {

    private final GoogleProperties properties;
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
                .queryParam("scope", properties.getScope())
                .queryParam("response_type", "code")
                .toUriString();
    }

    @Override
    @Transactional
    public OauthResponse createToken(String code) {
        GoogleDto oauthToken = getOauthToken(code);
        GoogleUserDto userInfo = getUserInfo(oauthToken.getAccessToken());

        Map<String, Object> extractClaims = new HashMap<>();
        extractClaims.put("oauthId", userInfo.getId());

        String accessToken = jwtProvider.generateAccessToken(extractClaims);
        String refreshToken = jwtProvider.generateRefreshToken(extractClaims);

        Users users = userRepository.findByOauthId(userInfo.getId())
                .orElseGet(() ->
                        Users.builder()
                                .oauthId(userInfo.getId())
                                .email(userInfo.getEmail())
                                .profileUrl(userInfo.getPicture())
                                .state(UserAuthenticateState.INCOMPLETE)
                                .oauthType(properties.getOauthType())
                                .build()
                );

        users.changeUser(
                users.toBuilder()
                        .refreshToken(refreshToken)
                        .build()
        );
        users = userRepository.save(users);

        return OauthResponse.builder()
                .userId(users.getId())
                .state(users.getState())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public GoogleDto getOauthToken(String code) {
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", properties.getClientId());
        params.add("client_secret", properties.getSecretKey());
        params.add("redirect_uri", properties.getRedirectUrl());
        params.add("grant_type", "authorization_code");

        return WebClient.create().post()
                .uri(properties.getTokenUrl())
                .bodyValue(params)
                .retrieve()
                .bodyToMono(GoogleDto.class)
                .onErrorMap(JsonProcessingException.class, e -> new IllegalArgumentException("Json Parse Error."))
                .block();
    }

    @Override
    public GoogleUserDto getUserInfo(String oauthToken) {
        return WebClient.create().get()
                .uri(properties.getUserInfoUrl())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + oauthToken)
                .retrieve()
                .bodyToMono(GoogleUserDto.class)
                .onErrorMap(JsonProcessingException.class, e -> new IllegalArgumentException("Json Parse Error."))
                .onErrorMap(WebClientResponseException.class, e -> new IllegalArgumentException(e.getStatusCode() + " 유저 정보 가져오는 과정에서 오류 발생"))
                .block();
    }

}
