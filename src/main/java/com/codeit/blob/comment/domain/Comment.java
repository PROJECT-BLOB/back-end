package com.codeit.blob.comment.domain;

import com.codeit.blob.global.domain.BaseTimeEntity;
import com.codeit.blob.post.domain.Post;
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
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id")
    private Users author;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent", orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Comment> reply;

    @Builder
    public Comment(String content, Users author, Post post, Comment parent){
        this.content = content;
        this.author = author;
        this.post = post;
        this.parent = parent;
        this.reply = new ArrayList<>();
    }

}
