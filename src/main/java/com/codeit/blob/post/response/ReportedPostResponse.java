package com.codeit.blob.post.response;

import com.codeit.blob.global.converter.DateTimeUtils;
import com.codeit.blob.post.domain.Post;
import com.codeit.blob.post.domain.PostImage;
import com.codeit.blob.user.response.UserProfileResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Getter
@Schema(name = "신고된 게시글 응답")
public class ReportedPostResponse implements PostResponse {

    private final Long postId;
    private final String title;
    private final String content;
    private final UserProfileResponse author;
    private final Long views;
    @Schema(example = "2024-04-24T12:59:24")
    private final String createdDate;
    private final List<String> imageUrl;
    private final int likeCount;
    private final int commentCount;
    private final int bookmarkCount;
    private final int reportCount;

    public ReportedPostResponse(Post post){
        this.postId = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.author = post.getAuthor() == null ? null : UserProfileResponse.of(post.getAuthor());
        this.views = post.getViews();
        this.createdDate = DateTimeUtils.format(post.getCreatedDate());
        this.imageUrl = post.getPostImages().stream().map(PostImage::getUrl).toList();
        this.likeCount = post.getLikes().size();
        this.commentCount = post.getComments().size();
        this.bookmarkCount = post.getBookmarks().size();
        this.reportCount = post.getBookmarks().size();
    }
}
