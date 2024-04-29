package com.codeit.blob.comment.domain;

import com.codeit.blob.global.domain.BaseTimeEntity;
import com.codeit.blob.post.domain.Post;
import com.codeit.blob.user.domain.Users;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id")
    private Users author;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Comment> reply;

    @OneToMany(mappedBy = "comment", fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<CommentLike> likes = new HashSet<>();

    @Builder
    public Comment(String content, Users author, Post post, Comment parent){
        this.content = content;
        this.author = author;
        this.post = post;
        this.parent = parent;
        this.reply = new ArrayList<>();
    }

    public void addLike(CommentLike like){
        likes.add(like);
    }

    public void removeLike(CommentLike like){
        likes.remove(like);
    }

}
