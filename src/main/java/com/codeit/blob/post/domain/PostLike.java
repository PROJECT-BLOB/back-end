package com.codeit.blob.post.domain;

import com.codeit.blob.user.domain.Users;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
}
