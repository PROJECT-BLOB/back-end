package com.codeit.blob.oauth.dto.kakao;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
// TODO KDY Json Property 설정 필요
public class KakaoUserDto {
    private String id;
    private KakaoAccount kakaoAccount;

    public String getEmail() {
        return kakaoAccount.getEmail();
    }

    public String getName() {
        return "";
    }

    public String getProfile() {
        return kakaoAccount.getProfile().getProfile_image_url();
    }

    @Getter
    public static class KakaoAccount {
        private boolean profile_nickname_needs_agreement;
        private boolean profile_image_needs_agreement;
        private KakaoProfile profile;
        private boolean has_email;
        private boolean email_needs_agreement;
        private boolean is_email_valid;
        private boolean is_email_verified;
        private String email;

        @Getter
        public static class KakaoProfile {
            private String thumbnail_image_url;
            private String profile_image_url;
            private boolean is_default_image;
        }
    }
}
