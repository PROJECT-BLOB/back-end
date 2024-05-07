package com.codeit.blob.post.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name="지도 조건 검색")
public class MapFilter {

    private List<String> categories;

    private double maxLat;

    private double minLat;

    private double maxLng;

    private double minLng;

}
