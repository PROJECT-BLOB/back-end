package com.codeit.blob.domain.post.domain.enums;

import java.util.Arrays;

public enum Subcategory {
    // todo add finalised list
    WEATHER("날씨");

    private String name;

    Subcategory(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Subcategory getInstance(String subcategory) {
        return Arrays.stream(Subcategory.values()).filter(c -> c.name().equals(subcategory))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("존재하지 않는 세부 카테고리입니다."));
    }
}
