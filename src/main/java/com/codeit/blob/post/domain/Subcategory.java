package com.codeit.blob.post.domain;

import com.codeit.blob.global.exceptions.CustomException;
import com.codeit.blob.global.exceptions.ErrorCode;

import java.util.Arrays;

public enum Subcategory {

    WEATHER("날씨"),
    RESTAURANT("음식점"),
    ACCOMMODATION("숙소"),
    HOSPITAL("병원"),
    TOILET("화장실"),
    PHARMACY("약국"),
    TRANSPORT("교통"),
    MUSEUM("박물관"),
    ATTRACTIONS("관광지"),
    ATM("ATM");

    private String label;

    Subcategory(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static Subcategory getInstance(String subcategory) {
        return Arrays.stream(Subcategory.values()).filter(c -> c.name().equals(subcategory))
                .findFirst().orElseThrow(() -> new CustomException(ErrorCode.SUBCATEGORY_NOT_FOUND));
    }
}
