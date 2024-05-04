package com.codeit.blob.user.response;

import com.codeit.blob.global.domain.Coordinate;
import com.codeit.blob.oauth.OauthType;
import com.codeit.blob.user.UserAuthenticateState;
import com.codeit.blob.user.domain.UserRole;
import com.codeit.blob.user.domain.Users;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(name = "유저 조회 응답 데이터")
public class UserResponse {
    private final Long userId;
    private final String email;
    private final String blobId;
    private final String nickName;
    private final String profileUrl;
    private final String bio;
    private final Integer postCount;
    private final Integer likedCount;
    private final Integer commentCount;

    @Schema(description = "유저 정보 공개 / 비공개 상태")
    private final Boolean isPrivate;

    private final Coordinate coordinate;

    @Schema(description = "유저 계정 상태")
    private final UserAuthenticateState state;

    @Schema(description = "유저 Oauth 로그인 타입")
    private final OauthType oauthType;

    @Schema(description = "유저 권한")
    private final UserRole role;

    public UserResponse(Users users) {
        this.userId = users.getId();
        this.email = users.getEmail();
        this.blobId = users.getBlobId();
        this.nickName = users.getNickName();
        this.profileUrl = users.getProfileUrl();
        this.state = users.getState();
        this.isPrivate = users.getIsPrivate();
        this.coordinate = users.getCoordinate();
        this.oauthType = users.getOauthType();
        this.role = users.getRole();
        this.bio = users.getBio();
        this.postCount = users.getPostCount();
        this.likedCount = users.getLikeCount();
        this.commentCount = users.getCommentCount();
    }
}
