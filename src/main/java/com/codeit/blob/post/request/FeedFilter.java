package com.codeit.blob.post.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name="피드 조건 검색")
public class FeedFilter {

    @NotEmpty
    private Double cityLat;

    @NotEmpty
    private Double cityLng;

    private String sortBy = "recent";

    private int page = 0;

    private int size = 10;

    private List<String> categories;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private boolean hasImage = false;

    private boolean hasLocation = false;

    private int minLikes = 0;

    private String keyword;

}
