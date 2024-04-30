package com.codeit.blob.post.response;

import com.codeit.blob.post.domain.Post;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@AllArgsConstructor
@Schema(name = "지도 게시글 사이드바 페이지 응답")
public class PostMapPageResponse {

    private final List<PostMapResponse> content;
    private final long count;
    private final long currentPage;
    private final boolean hasMore;

    public PostMapPageResponse(Page<Post> page){
        this.content = page.getContent().stream().map(PostMapResponse::new).toList();
        this.count = page.getTotalElements();
        this.currentPage = page.getNumber();
        this.hasMore = page.hasNext();
    }
}
