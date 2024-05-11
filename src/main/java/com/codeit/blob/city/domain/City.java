package com.codeit.blob.city.domain;

import com.codeit.blob.city.domain.Country;
import com.codeit.blob.global.domain.Coordinate;
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

    private String name;

    @Embedded
    private Coordinate coordinate;

    @Builder
    public City(
            Country country,
            String name,
            Coordinate coordinate
    ){
        this.country = country;
        this.name = name;
        this.coordinate = coordinate;
    }
}
