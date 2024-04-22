package com.codeit.blob.user.request;

import com.codeit.blob.user.UserAuthenticateType;
import com.codeit.blob.oauth.OauthType;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserRequest {
    private final String oauthId;
    private final String email;
    private final String blobId;
    private final String nickName;
    private final String profileUrl;
    private final UserAuthenticateType userAuthenticateType;
    private final OauthType oauthType;

    @Builder
    public UserRequest(String oauthId, String email, String blobId, String nickName, String profileUrl, UserAuthenticateType userAuthenticateType, OauthType oauthType) {
        this.oauthId = oauthId;
        this.email = email;
        this.blobId = blobId;
        this.nickName = nickName;
        this.profileUrl = profileUrl;
        this.userAuthenticateType = userAuthenticateType;
        this.oauthType = oauthType;
    }
}
