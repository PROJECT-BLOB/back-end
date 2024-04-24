package com.codeit.blob.post.domain;

import com.codeit.blob.city.domain.City;
import com.codeit.blob.global.domain.BaseTimeEntity;
import com.codeit.blob.user.domain.Users;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Enumerated(EnumType.STRING)
    private Subcategory subcategory;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id")
    private Users author;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "city_id")
    private City city;

    @Column(columnDefinition = "POINT SRID 4326")
    private Point location;

    private Long distFromActual;

    @OneToMany(mappedBy = "post", orphanRemoval = true)
    private List<PostImage> postImages = new ArrayList<>();

    private Long views;

    @Builder
    public Post(
            String title,
            String content,
            Category category,
            Subcategory subcategory,
            Users author,
            City city,
            Point location,
            Point actualLocation
    ) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.subcategory = subcategory;
        this.author = author;
        this.city = city;
        this.location = location;
        this.distFromActual = calculateDistance(location, actualLocation);
        this.views = 0L;
    }

    public void incrementView(){
        this.views++;
    }

    public void addImage(PostImage img){
        postImages.add(img);
        img.setPost(this);
    }

    private Long calculateDistance(Point point1, Point point2){
        if (point1 == null || point2 == null) return null;

        // Haversine formula
        double earthRadius = 6371000;

        double lat1 = Math.toRadians(point1.getY());
        double lon1 = Math.toRadians(point1.getX());
        double lat2 = Math.toRadians(point2.getY());
        double lon2 = Math.toRadians(point2.getX());

        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(lat1) * Math.cos(lat2) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return Math.round(earthRadius * c);
    }



}
