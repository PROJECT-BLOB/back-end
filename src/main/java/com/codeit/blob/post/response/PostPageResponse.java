package com.codeit.blob.post.response;

import com.codeit.blob.post.domain.Post;
import com.codeit.blob.user.domain.Users;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@AllArgsConstructor
@Schema(name = "게시글 페이지 응답")
public class PostPageResponse {

    private final List<PostResponse> content;
    private final long count;
    private final long currentPage;
    private final boolean hasMore;

    public PostPageResponse(Page<Post> page, Users user){
        this.content = page.getContent().stream().map(p -> new PostResponse(p, user)).toList();
        this.count = page.getTotalElements();
        this.currentPage = page.getNumber();
        this.hasMore = page.hasNext();
    }
}
