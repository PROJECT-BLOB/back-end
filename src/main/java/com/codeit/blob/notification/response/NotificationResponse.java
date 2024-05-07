package com.codeit.blob.notification.response;

import com.codeit.blob.notification.domain.Notification;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(name = "알림 데이터 응답")
public class NotificationResponse {

    private final Long notificationId;
    private final Long postId;
    private final String message;

    public NotificationResponse(Notification notification){
        this.notificationId = notification.getId();
        this.postId = notification.getPost().getId();
        this.message = notification.getMessage();
    }
}
