package com.codeit.blob.user;

import lombok.Getter;

@Getter
public enum UserAuthenticateState {
    COMPLETE("사용 가능한 아이디"),
    INCOMPLETE("추가적인 인증이 필요한 아이디");

    private final String description;

    UserAuthenticateState(String description) {
        this.description = description;
    }
}
