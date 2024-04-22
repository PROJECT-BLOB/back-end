package com.codeit.blob.oauth.dto.naver;

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
        this.profile = response.getProfile_image();
    }

    @Getter
    private static class NaverResponse {
        private String id;
        private String nickname;
        private String profile_image;
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
