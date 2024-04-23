package com.codeit.blob.post.domain.entity;

import com.codeit.blob.post.domain.enums.Country;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Country country;

    private String code;

    private String name;

    @Builder
    public City(
            Country country,
            String code,
            String name
    ){
        this.country = country;
        this.code = code;
        this.name = name;
    }
}
