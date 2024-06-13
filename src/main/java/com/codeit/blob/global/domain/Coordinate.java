package com.codeit.blob.global.domain;

import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Embeddable
@Getter
@NoArgsConstructor
public class Coordinate {

    private double lat;
    private double lng;

    @Builder
    public Coordinate(double lat, double lng){
        this.lat = lat;
        this.lng = lng;
    }

    public Long calculateDistance(double lat2, double lon2){
        // Haversine formula
        double earthRadius = 6371000;

        double dLat = toRad(lat2 - this.getLat());
        double dLon =  toRad(lon2 - this.getLng());

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(this.getLat()) * Math.cos(lat2) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return Math.round(earthRadius * c);
    }

    public Double toRad(Double value) {
        return value * Math.PI / 180;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Coordinate)) return false;
        Coordinate coordinate = (Coordinate) o;
        return lat == coordinate.getLat() && lng == coordinate.getLng();
    }

    @Override
    public int hashCode() {
        return Objects.hash(lat, lng);
    }
}
