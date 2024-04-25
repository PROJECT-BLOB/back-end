package com.codeit.blob.oauth.dto.naver;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
public class NaverUserDto {
    private final String resultcode;
    private final String message;
    private final String id;
    private final String name;
    private final String email;
    private final String profile;

    public NaverUserDto(String resultcode, String message, NaverResponse response) {
        this.resultcode = resultcode;
        this.message = message;
        this.id = response.getId();
        this.name = response.getName();
        this.email = response.getEmail();
        this.profile = response.getProfileImage();
    }

    @Getter
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    private static class NaverResponse {
        private String id;
        private String nickname;
        private String profileImage;
        private String email;
        private String name;
    }

    /**
     *<pre>
     * 네이버에서 제공하는 형식
     * {
     *   "resultcode": "00",
     *   "message": "success",
     *   "response": {
     *     "id": "oauthId",
     *     "nickname": "닉네임",
     *     "profile_image": "profile_image url",
     *     "email": "email",
     *     "name": "fullname"
     *   },
     * }
     * </pre>
     * */
}
