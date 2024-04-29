package com.codeit.blob.comment.response;

import com.codeit.blob.comment.domain.Comment;
import com.codeit.blob.user.domain.Users;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.util.List;

@Schema(name = "댓글 페이지 응답")
public class CommentPageResponse {

    private final List<CommentResponse> content;
    private final long count;
    private final int currentPage;
    private final boolean hasMore;

    public CommentPageResponse(Page<Comment> page, Users user){
        this.content = page.getContent().stream().map(c -> new CommentResponse(c, user)).toList();
        this.count = page.getTotalElements();
        this.currentPage = page.getNumber();
        this.hasMore = page.hasNext();
    }
}
