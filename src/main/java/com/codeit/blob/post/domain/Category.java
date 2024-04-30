package com.codeit.blob.post.domain;

import com.codeit.blob.global.exceptions.CustomException;
import com.codeit.blob.global.exceptions.ErrorCode;

import java.util.Arrays;

public enum Category {
    RECOMMENDED("추천"),
    NOT_RECOMMENDED("비추천"),
    QUESTION("질문"),
    WARNING("주의"),
    HELP("도움요청");

    private String label;

    Category(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static Category getInstance(String category) {
        return Arrays.stream(Category.values()).filter(c -> c.name().equals(category))
                .findFirst().orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
    }
}
