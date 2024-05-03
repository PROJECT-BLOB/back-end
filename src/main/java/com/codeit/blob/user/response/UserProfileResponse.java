package com.codeit.blob.user.response;

import com.codeit.blob.user.domain.Users;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(name = "유저 프로필 정보 응답")
public class UserProfileResponse {

    private final Long userId;
    private final String blobId;
    private final String nickname;
    private final String profileUrl;
    private final Integer likedCount;

    public UserProfileResponse(Users user){
        this.userId = user.getId();
        this.blobId = user.getBlobId();
        this.nickname = user.getNickName();
        this.profileUrl = user.getProfileUrl();
        this.likedCount = user.getLikeCount();
    }

}
