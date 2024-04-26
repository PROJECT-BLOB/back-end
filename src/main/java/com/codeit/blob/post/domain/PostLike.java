package com.codeit.blob.post.domain;

import com.codeit.blob.user.domain.Users;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    private Users user;

    @ManyToOne
    private Post post;

    public PostLike(Users user, Post post) {
        this.user = user;
        this.post = post;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PostLike)) return false;
        PostLike postLike = (PostLike) o;
        return user.getId().equals(postLike.getUser().getId()) &&
                post.getId().equals(postLike.getPost().getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(user.getId(), post.getId());
    }
}
