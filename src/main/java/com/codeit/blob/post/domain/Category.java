package com.codeit.blob.post.domain;

import java.util.Arrays;

public enum Category {
    // todo add finalised list
    WARNING("조심하세요"),
    INFO("참고하세요"),
    QUESTION("궁금해요"),
    HELP("도와주세요");

    private String label;

    Category(String name) {
        this.label = name;
    }

    public String getLabel() {
        return label;
    }

    public static Category getInstance(String category) {
        return Arrays.stream(Category.values()).filter(c -> c.getLabel().equals(category))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다."));
    }
}
