package com.codeit.blob.user.response;

import com.codeit.blob.user.UserAuthenticateState;
import com.codeit.blob.user.domain.Users;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(name = "유저 조회 응답 데이터")
public class UserResponse {
    private final String email;
    private final String blobId;
    private final String nickName;
    private final String profileUrl;
    private final UserAuthenticateState state;

    public UserResponse(Users users) {
        this.email = users.getEmail();
        this.blobId = users.getBlobId();
        this.nickName = users.getNickName();
        this.profileUrl = users.getProfileUrl();
        this.state = users.getState();
    }
}
