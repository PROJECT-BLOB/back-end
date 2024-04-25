package com.codeit.blob.user.response;

import com.codeit.blob.user.UserAuthenticateState;
import com.codeit.blob.user.domain.Users;
import lombok.Getter;

@Getter
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
