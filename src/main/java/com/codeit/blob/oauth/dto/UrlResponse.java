package com.codeit.blob.oauth.dto;

import lombok.Getter;

@Getter
public class UrlResponse {
    private final String redirectUrl;

    public UrlResponse(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
}
