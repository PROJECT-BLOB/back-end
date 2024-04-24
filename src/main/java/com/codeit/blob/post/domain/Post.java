package com.codeit.blob.post.domain;

import com.codeit.blob.city.domain.City;
import com.codeit.blob.global.domain.BaseTimeEntity;
import com.codeit.blob.user.domain.Users;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Embedded
    private Coordinate coordinate;

    private Long distFromActual;

    @OneToMany(mappedBy = "post", orphanRemoval = true)
    private List<PostImage> postImages = new ArrayList<>();

    private long views = 0;

    @Builder
    public Post(
            String title,
            String content,
            Category category,
            Subcategory subcategory,
            Users author,
            City city,
            Coordinate coordinate,
            Long distFromActual
    ) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.subcategory = subcategory;
        this.author = author;
        this.city = city;
        this.coordinate = coordinate;
        this.distFromActual = distFromActual;
    }

    public void incrementView(){
        this.views++;
    }

    public void addImage(PostImage img){
        postImages.add(img);
        img.setPost(this);
    }



}
