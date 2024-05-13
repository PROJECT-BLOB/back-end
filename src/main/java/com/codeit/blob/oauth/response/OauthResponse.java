package com.codeit.blob.oauth.response;

import com.codeit.blob.user.UserState;
import com.codeit.blob.user.domain.Users;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Schema(name = "Oauth 로그인 성공 응답 데이터")
public class OauthResponse {

    private final Long userId;
    private final String blobId;
    private final String accessToken;
    private final String refreshToken;
    private final UserState state;

    @Builder
    public OauthResponse(Users user, String accessToken, String refreshToken, UserState state) {
        this.userId = user.getId();
        this.blobId = user.getBlobId();
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.state = state;
    }
}
