package com.codeit.blob.post.domain;

import com.codeit.blob.global.domain.BaseTimeEntity;
import com.codeit.blob.post.domain.Post;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class PostImage extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    public PostImage(String url) {
        this.url = url;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
