package com.codeit.blob.post.domain;

import com.codeit.blob.city.domain.City;
import com.codeit.blob.global.domain.BaseTimeEntity;
import com.codeit.blob.post.domain.Post;
import com.codeit.blob.user.domain.Users;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor
public class Bookmark extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "city_id")
    private City city;

    @Builder
    public Bookmark(Users user, Post post){
        this.user = user;
        this.post = post;
        this.city = post.getCity();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bookmark)) return false;
        Bookmark postLike = (Bookmark) o;
        return user.getId().equals(postLike.getUser().getId()) &&
                post.getId().equals(postLike.getPost().getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(user.getId(), post.getId());
    }

}
