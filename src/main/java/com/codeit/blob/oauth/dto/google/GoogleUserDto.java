package com.codeit.blob.oauth.dto.google;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GoogleUserDto {
    private final String id;
    private final String email;
    private final String verifiedEmail;
    private final String name;
    private final String givenName;
    private final String familyName;
    private final String picture;
    private final String locale;

    public GoogleUserDto(String id, String email, String verifiedEmail, String name, String givenName, String familyName, String picture, String locale) {
        this.id = id;
        this.email = email;
        this.verifiedEmail = verifiedEmail;
        this.name = name;
        this.givenName = givenName;
        this.familyName = familyName;
        this.picture = picture;
        this.locale = locale;
    }
}
