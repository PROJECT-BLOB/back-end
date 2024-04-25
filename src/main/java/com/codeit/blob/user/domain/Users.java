package com.codeit.blob.user.domain;

import com.codeit.blob.global.domain.BaseTimeEntity;
import com.codeit.blob.oauth.OauthType;
import com.codeit.blob.user.UserAuthenticateState;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Users extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String oauthId;
    private String blobId;
    private String nickName;
    private String profileUrl;
    private String refreshToken;

    @Enumerated(EnumType.STRING)
    private UserAuthenticateState state;

    @Enumerated(EnumType.STRING)
    private OauthType oauthType;


    @Builder(toBuilder = true)
    public Users(String email, String oauthId, String blobId, String nickName, String profileUrl, String refreshToken, UserAuthenticateState state, OauthType oauthType) {
        this.email = email;
        this.oauthId = oauthId;
        this.blobId = blobId;
        this.nickName = nickName;
        this.profileUrl = profileUrl;
        this.refreshToken = refreshToken;
        this.state = state;
        this.oauthType = oauthType;
    }

    public void changeUser(Users users) {
        this.email = users.getEmail();
        this.oauthId = users.getOauthId();
        this.blobId = users.getBlobId();
        this.nickName = users.getNickName();
        this.profileUrl = users.getProfileUrl();
        this.refreshToken = users.getRefreshToken();
        this.state = users.getState();
        this.oauthType = users.getOauthType();
    }
}
