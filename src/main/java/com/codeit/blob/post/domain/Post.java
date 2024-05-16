package com.codeit.blob.post.domain;

import com.codeit.blob.city.domain.City;
import com.codeit.blob.comment.domain.Comment;
import com.codeit.blob.global.domain.BaseTimeEntity;
import com.codeit.blob.global.domain.Coordinate;
import com.codeit.blob.notification.domain.Notification;
import com.codeit.blob.user.domain.Users;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
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

    @Column(columnDefinition = "TEXT")
    private String address;

    private Long distFromActual;

    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER, orphanRemoval = true)
    private List<PostImage> postImages = new ArrayList<>();

    private Long views;

    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<PostLike> likes = new HashSet<>();

    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<Bookmark> bookmarks = new HashSet<>();

    @OneToMany(mappedBy = "post", orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", orphanRemoval = true)
    private Set<PostReport> reports = new HashSet<>();

    @OneToMany(mappedBy = "post", orphanRemoval = true)
    private Set<Notification> notifications = new HashSet<>();

    @Builder
    public Post(
            String title,
            String content,
            Category category,
            Subcategory subcategory,
            Users author,
            City city,
            Coordinate coordinate,
            String address,
            Long distFromActual
    ) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.subcategory = subcategory;
        this.author = author;
        this.city = city;
        this.coordinate = coordinate;
        this.address = address;
        this.distFromActual = distFromActual;
        this.views = 0L;
    }

    public void incrementView(){
        this.views++;
    }

    public void addImage(PostImage image){
        postImages.add(image);
    }

    public void addLike(PostLike like){
        likes.add(like);
    }

    public void removeLike(PostLike like){
        likes.remove(like);
    }

    public void addBookmark(Bookmark bookmark){
        bookmarks.add(bookmark);
    }

    public void removeBookmark(Bookmark bookmark){
        bookmarks.remove(bookmark);
    }

    public LocalDateTime getExpiresAt(){
        return getCreatedDate().plusHours(24).plusMinutes(30L * likes.size());
    }

}
