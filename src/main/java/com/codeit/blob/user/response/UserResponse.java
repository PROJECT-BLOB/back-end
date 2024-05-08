package com.codeit.blob.user.response;

import com.codeit.blob.global.domain.Coordinate;
import com.codeit.blob.oauth.OauthType;
import com.codeit.blob.user.UserState;
import com.codeit.blob.user.domain.UserRole;
import com.codeit.blob.user.domain.Users;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
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
    private final UserState state;

    @Schema(description = "유저 Oauth 로그인 타입")
    private final OauthType oauthType;

    @Schema(description = "유저 권한")
    private final UserRole role;

    public static UserResponse of(Users users){
        if(users.getState().equals(UserState.DELETED)) return null;

        return new UserResponse(
                users.getId(),
                users.getEmail(),
                users.getBlobId(),
                users.getNickName(),
                users.getProfileUrl(),
                users.getBio(),
                users.getPostCount(),
                users.getLikeCount(),
                users.getCommentCount(),
                users.getIsPrivate(),
                users.getCoordinate(),
                users.getState(),
                users.getOauthType(),
                users.getRole()
        );
    }
}
