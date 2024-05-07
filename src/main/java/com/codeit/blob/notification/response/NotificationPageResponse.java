package com.codeit.blob.notification.response;

import com.codeit.blob.notification.domain.Notification;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@AllArgsConstructor
@Schema(name = "알림 페이지 응답")
public class NotificationPageResponse {

    private final List<NotificationResponse> content;
    private final long count;
    private final long currentPage;
    private final boolean hasMore;

    public NotificationPageResponse(Page<Notification> page){
        this.content = page.getContent().stream().map(NotificationResponse::new).toList();
        this.count = page.getTotalElements();
        this.currentPage = page.getNumber();
        this.hasMore = page.hasNext();
    }
}
