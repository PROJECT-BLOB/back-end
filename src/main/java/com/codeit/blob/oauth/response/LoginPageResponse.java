package com.codeit.blob.oauth.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(name = "Oauth 로그인 페이지 주소 응답 데이터")
public class LoginPageResponse {
    private final String redirectUrl;

    public LoginPageResponse(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
}
