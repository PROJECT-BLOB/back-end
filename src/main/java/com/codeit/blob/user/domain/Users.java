package com.codeit.blob.user.domain;

import com.codeit.blob.user.UserAuthenticateType;
import com.codeit.blob.global.domain.BaseTimeEntity;
import com.codeit.blob.oauth.OauthType;
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

    @Enumerated(EnumType.STRING)
    private UserAuthenticateType userAuthenticateType;

    @Enumerated(EnumType.STRING)
    private OauthType oauthType;

    @Builder
    public Users(String email, String oauthId, String blobId, String nickName, String profileUrl, UserAuthenticateType userAuthenticateType, OauthType oauthType) {
        this.email = email;
        this.oauthId = oauthId;
        this.blobId = blobId;
        this.nickName = nickName;
        this.profileUrl = profileUrl;
        this.userAuthenticateType = userAuthenticateType;
        this.oauthType = oauthType;
    }
}
