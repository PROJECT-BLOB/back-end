package com.codeit.blob.user.response;

import com.codeit.blob.global.domain.Coordinate;
import com.codeit.blob.oauth.OauthType;
import com.codeit.blob.user.UserState;
import com.codeit.blob.user.domain.UserRole;
import com.codeit.blob.user.domain.Users;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(name = "유저 조회 응답 데이터")
public class UserResponse {

    private Long userId;
    private String email;
    private String blobId;
    private String nickName;
    private String profileUrl;
    private String bio;
    private Integer postCount;
    private Integer likedCount;
    private Integer commentCount;

    @Schema(description = "유저 정보 공개 / 비공개 상태")
    private Boolean isPrivate;

    private Coordinate coordinate;

    @Schema(description = "유저 계정 상태")
    private UserState state;

    @Schema(description = "유저 Oauth 로그인 타입")
    private OauthType oauthType;

    @Schema(description = "유저 권한")
    private UserRole role;

    public UserResponse(Users users) {
        if(users.getState().equals(UserState.DELETED)) return;

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
