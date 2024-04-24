package com.codeit.blob.user.request;

import com.codeit.blob.user.UserAuthenticateState;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserUpdateRequest {
    private final String oauthId;
    private final String blobId;
    private final String nickName;
    private final UserAuthenticateState userAuthenticateState;

    @Builder
    public UserUpdateRequest(String oauthId, String blobId, String nickName, UserAuthenticateState userAuthenticateState) {
        this.oauthId = oauthId;
        this.blobId = blobId;
        this.nickName = nickName;
        this.userAuthenticateState = userAuthenticateState;
    }
}
