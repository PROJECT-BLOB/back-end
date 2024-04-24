package com.codeit.blob.jwt.provider;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CookieProvider {
    public ResponseCookie createCookie(String cookieName, String data) {
        return ResponseCookie.from(cookieName, data)
                .path("/")
                .httpOnly(true)
                .build();
    }
}
