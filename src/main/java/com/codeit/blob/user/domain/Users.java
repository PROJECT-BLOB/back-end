package com.codeit.blob.user.domain;

import com.codeit.blob.comment.domain.Comment;
import com.codeit.blob.global.domain.BaseTimeEntity;
import com.codeit.blob.global.domain.Coordinate;
import com.codeit.blob.oauth.OauthType;
import com.codeit.blob.post.domain.Post;
import com.codeit.blob.post.domain.PostLike;
import com.codeit.blob.user.UserState;
import com.codeit.blob.user.request.UserRequest;
import com.codeit.blob.user.request.UserUpdateRequest;
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
    private String nickname;
    private String profileUrl;
    private Boolean isPublic;
    private String bio;
    private String refreshToken;

    @Embedded
    private Coordinate coordinate;

    @Enumerated(EnumType.STRING)
    private UserState state;

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
    public Users(String email, String oauthId, String blobId, String nickname, String profileUrl, String bio, String refreshToken, boolean isPublic, Coordinate coordinate, UserState state, OauthType oauthType) {
        this.email = email;
        this.oauthId = oauthId;
        this.blobId = blobId;
        this.nickname = nickname;
        this.profileUrl = profileUrl;
        this.bio = bio;
        this.refreshToken = refreshToken;
        this.isPublic = isPublic;
        this.coordinate = coordinate;
        this.state = state;
        this.oauthType = oauthType;
        this.role = UserRole.ROLE_USER;
    }

    public void updateUser(UserUpdateRequest request, String profileUrl) {
        if (request.getNickname() != null) this.nickname = request.getNickname();
        if (request.getBio() != null) this.bio = request.getBio();
        if (request.getIsPublic() != null) this.isPublic = request.getIsPublic();
        if (request.getLat() != null && request.getLng() != null) this.coordinate = new Coordinate(request.getLat(), request.getLng());
        this.profileUrl = profileUrl;
    }

    public void validateUser(UserRequest request) {
        this.blobId = request.getBlobId();
        this.nickname = request.getNickname();
        this.state = UserState.COMPLETE;
        this.bio = "안녕하세요. 여행을 좋아하는 블로비라고 합니다. 좋은 정보를 공유합니다. 즐겁게 여행해요";
        this.isPublic = true;
    }

    public void setRefreshToken(String token){
        this.refreshToken = token;
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

    public void deleteUser(){
        this.email = null;
        this.oauthId = null;
        this.blobId = null;
        this.nickname = null;
        this.profileUrl = null;
        this.bio = null;
        this.refreshToken = null;
        this.coordinate = null;
        this.state = UserState.DELETED;
    }

    public void deleteProfileUrl(){
        this.profileUrl = null;
    }
}
