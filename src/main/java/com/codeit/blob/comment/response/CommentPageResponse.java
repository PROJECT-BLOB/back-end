package com.codeit.blob.comment.response;

import com.codeit.blob.comment.domain.Comment;
import com.codeit.blob.user.domain.Users;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Schema(name = "댓글 페이지 응답")
public class CommentPageResponse {

    private final List<CommentResponse> content;
    private final long count;
    private final long currentPage;
    private final boolean hasMore;
    private final long remaining;

    public CommentPageResponse(Page<Comment> page, Users user){
        this.content = page.getContent().stream().map(c -> new CommentResponse(c, user)).toList();
        this.count = page.getTotalElements();
        this.currentPage = page.getNumber();
        this.hasMore = page.hasNext();
        this.remaining = Math.max(0, page.getTotalElements() - (page.getNumber() + 1L) * page.getSize());
    }
}
