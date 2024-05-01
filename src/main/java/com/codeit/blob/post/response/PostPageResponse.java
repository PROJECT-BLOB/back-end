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

    private final List<? extends PostResponse> content;
    private final long count;
    private final long currentPage;
    private final boolean hasMore;

    public static PostPageResponse postDetailPageResponse(Page<Post> page, Users user){
        return new PostPageResponse(
                page.getContent().stream().map(p -> new DetailedPostResponse(p, user)).toList(),
                page.getTotalElements(),
                page.getNumber(),
                page.hasNext()
        );
    }

    public static PostPageResponse postMapPageResponse(Page<Post> page){
        return new PostPageResponse(
                page.getContent().stream().map(MapPostResponse::new).toList(),
                page.getTotalElements(),
                page.getNumber(),
                page.hasNext()
        );
    }

    public static PostPageResponse postReportedPageResponse(Page<Post> page){
        return new PostPageResponse(
                page.getContent().stream().map(ReportedPostResponse::new).toList(),
                page.getTotalElements(),
                page.getNumber(),
                page.hasNext()
        );
    }
}
