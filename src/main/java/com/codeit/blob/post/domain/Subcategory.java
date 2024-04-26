package com.codeit.blob.post.domain;

import java.util.Arrays;

public enum Subcategory {
    // todo add finalised list
    WEATHER("날씨"),
    ATM("ATM"),
    RESTAURANT("음식점"),
    ACCOMMODATION("숙소");

    private String label;

    Subcategory(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static Subcategory getInstance(String subcategory) {
        return Arrays.stream(Subcategory.values()).filter(c -> c.name().equals(subcategory))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("존재하지 않는 세부 카테고리입니다."));
    }
}
