package com.codeit.blob.user;

import lombok.Getter;

@Getter
public enum UserState {
    COMPLETE("사용 가능한 계정입니다."),
    INCOMPLETE("추가적인 인증이 필요한 계정입니다."),
    DELETED("삭제된 계정입니다.");

    private final String description;

    UserState(String description) {
        this.description = description;
    }
}
