package com.codeit.blob.user.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserRequest {
    private final String oauthId;
    private final String blobId;
    private final String nickName;

    @Builder
    public UserRequest(String oauthId, String blobId, String nickName) {
        this.oauthId = oauthId;
        this.blobId = blobId;
        this.nickName = nickName;
    }
}
