package com.codeit.blob.post.domain;

import com.codeit.blob.global.domain.BaseTimeEntity;
import com.codeit.blob.user.domain.Users;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostReport extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    private Users reporter;

    @ManyToOne(fetch = FetchType.EAGER)
    private Users reportedUser;

    @ManyToOne(fetch = FetchType.EAGER)
    private Post post;

    @Builder
    public PostReport(Users reporter, Users reportedUser, Post post){
        this.reporter = reporter;
        this.reportedUser = reportedUser;
        this.post = post;
    }
}
