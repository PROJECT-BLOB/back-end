package com.codeit.blob.oauth.dto.google;

import lombok.Getter;

@Getter
// TODO KDY Json Property 설정 필요
public class GoogleUserDto {
    private final String id;
    private final String email;
    private final String verified_email;
    private final String name;
    private final String given_name;
    private final String family_name;
    private final String picture;
    private final String locale;

    public GoogleUserDto(String id, String email, String verified_email, String name, String given_name, String family_name, String picture, String locale) {
        this.id = id;
        this.email = email;
        this.verified_email = verified_email;
        this.name = name;
        this.given_name = given_name;
        this.family_name = family_name;
        this.picture = picture;
        this.locale = locale;
    }
}
