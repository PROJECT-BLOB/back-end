package com.codeit.blob.user.response;

import com.codeit.blob.user.UserState;
import com.codeit.blob.user.domain.Users;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(name = "유저 프로필 정보 응답")
public class UserProfileResponse {

    private final Long userId;
    private final String blobId;
    private final String nickname;
    private final String profileUrl;
    private final Integer likedCount;

    public static UserProfileResponse of(Users user){
        if(user.getState().equals(UserState.DELETED)) return null;

        return new UserProfileResponse(
                user.getId(),
                user.getBlobId(),
                user.getNickName(),
                user.getProfileUrl(),
                user.getLikeCount()
        );
    }

}
