package com.codeit.blob.notification.domain;

import com.codeit.blob.comment.domain.Comment;
import com.codeit.blob.global.domain.BaseTimeEntity;
import com.codeit.blob.post.domain.Post;
import com.codeit.blob.post.domain.PostLike;
import com.codeit.blob.user.domain.Users;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Notification extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    private Users receiver;

    @ManyToOne(fetch = FetchType.EAGER)
    private Post post;

    @ManyToOne(fetch = FetchType.EAGER)
    private Comment comment;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @Column(columnDefinition = "TEXT")
    private String message;

    public Notification(PostLike like){
        this.receiver = like.getPost().getAuthor();
        this.post = like.getPost();
        this.type = NotificationType.LIKED_POST;
        this.message = like.getUser().getNickName() + "님이 회원님의 게시글을 좋아합니다.";
    }

    public Notification(Comment comment){
        if (comment.getParent() == null){
            this.receiver = comment.getPost().getAuthor();
            this.type = NotificationType.NEW_COMMENT;
            this.message = comment.getAuthor().getNickName() + "님이 댓글을 남겼습니다.: " + comment.getContent();
        } else {
            this.receiver = comment.getParent().getAuthor();
            this.type = NotificationType.NEW_REPLY;
            this.message = comment.getAuthor().getNickName() + "님이 답글을 남겼습니다.: " + comment.getContent();
        }
        this.post = comment.getPost();
        this.comment = comment;
    }
}
