package com.codeit.blob.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
@Schema(name = "유저 추가 인증 요청 데이터")
public class UserRequest {
    @NotEmpty(message = "Oauth Id 는 필수 데이터 입니다.")
    @Schema(example = "14685162935")
    private final String oauthId;

    @NotEmpty(message = "Blob Id 는 필수 데이터 입니다.")
    @Schema(example = "blobblob1234")
    private final String blobId;

    @NotEmpty(message = "Nick Name 는 필수 데이터 입니다.")
    @Schema(example = "코드코드")
    private final String nickName;

    public UserRequest(String oauthId, String blobId, String nickName) {
        this.oauthId = oauthId;
        this.blobId = blobId;
        this.nickName = nickName;
    }
}
