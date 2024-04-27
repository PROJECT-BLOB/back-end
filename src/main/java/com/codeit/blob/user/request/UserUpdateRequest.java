package com.codeit.blob.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Schema(name = "유저 정보 수정 요청 데이터")
public class UserUpdateRequest {
    @NotEmpty(message = "nickName 는 필수 데이터 입니다.")
    @Schema(description = "상세 위치 위도", example = "37.532600")
    private final String nickName;

    @Schema(description = "상세 위치 위도", example = "37.532600")
    private final Double lat;

    @Schema(description = "상세 위치 경도", example = "127.024612")
    private final Double lng;

    public UserUpdateRequest(String nickName, Double lat, Double lng) {
        this.nickName = nickName;
        this.lat = lat;
        this.lng = lng;
    }
}
