package com.codeit.blob.comment.domain;

import com.codeit.blob.user.domain.Users;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    private Users user;

    @ManyToOne
    private Comment comment;

    public CommentLike(Users user, Comment comment) {
        this.user = user;
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommentLike)) return false;
        CommentLike postLike = (CommentLike) o;
        return user.getId().equals(postLike.getUser().getId()) &&
                comment.getId().equals(postLike.getComment().getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(user.getId(), comment.getId());
    }
}
