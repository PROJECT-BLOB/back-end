package com.codeit.blob.post.domain;

import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

        double dLat = lat2 - this.getLat();
        double dLon = lon2 - this.getLng();

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(this.getLat()) * Math.cos(lat2) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return Math.round(earthRadius * c);
    }
}
