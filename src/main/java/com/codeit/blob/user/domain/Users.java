package com.codeit.blob.user.domain;

import com.codeit.blob.comment.domain.Comment;
import com.codeit.blob.global.domain.BaseTimeEntity;
import com.codeit.blob.global.domain.Coordinate;
import com.codeit.blob.oauth.OauthType;
import com.codeit.blob.post.domain.Post;
import com.codeit.blob.post.domain.PostLike;
import com.codeit.blob.user.UserAuthenticateState;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Users extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String oauthId;
    private String blobId;
    private String nickName;
    private String profileUrl;
    private Boolean isPrivate;
    private String bio;
    private String refreshToken;

    @Embedded
    private Coordinate coordinate;

    @Enumerated(EnumType.STRING)
    private UserAuthenticateState state;

    @Enumerated(EnumType.STRING)
    private OauthType oauthType;

    @Enumerated(EnumType.STRING)
    private UserRole role;


    @OneToMany(mappedBy = "author", fetch = FetchType.EAGER)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<PostLike> postLikes = new ArrayList<>();

    @OneToMany(mappedBy = "author", fetch = FetchType.EAGER)
    private List<Comment> comments = new ArrayList<>();

    @Builder(toBuilder = true)
    public Users(String email, String oauthId, String blobId, String nickName, String profileUrl, String bio, String refreshToken, boolean isPrivate, Coordinate coordinate, UserAuthenticateState state, OauthType oauthType) {
        this.email = email;
        this.oauthId = oauthId;
        this.blobId = blobId;
        this.nickName = nickName;
        this.profileUrl = profileUrl;
        this.bio = bio;
        this.refreshToken = refreshToken;
        this.isPrivate = isPrivate;
        this.coordinate = coordinate;
        this.state = state;
        this.oauthType = oauthType;
        this.role = UserRole.ROLE_USER;
    }

    public void changeUser(Users users) {
        this.email = users.getEmail();
        this.oauthId = users.getOauthId();
        this.blobId = users.getBlobId();
        this.nickName = users.getNickName();
        this.profileUrl = users.getProfileUrl();
        this.refreshToken = users.getRefreshToken();
        this.isPrivate = users.getIsPrivate();
        this.state = users.getState();
        this.oauthType = users.getOauthType();
        this.coordinate = users.getCoordinate();
        this.bio = users.getBio();
    }

    public Integer getPostCount() {
        return posts.size();
    }

    public Integer getLikeCount() {
        return postLikes.size();
    }

    public Integer getCommentCount() {
        return comments.size();
    }

    public Users makeAdmin() {
        this.role = UserRole.ROLE_ADMIN;
        return this;
    }
}
