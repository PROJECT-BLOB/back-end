package com.codeit.blob.notification.controller;

import com.codeit.blob.notification.response.NotificationPageResponse;
import com.codeit.blob.notification.service.NotificationService;
import com.codeit.blob.oauth.domain.CustomUsers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
@Tag(name = "알림 API", description = "알림 관련 API")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    @Operation(summary = "내 알림 조회 API", description = "로그인된 유저의 알림을 조회합니다.")
    public ResponseEntity<NotificationPageResponse> getReportedPosts(
            @AuthenticationPrincipal CustomUsers customUsers,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return ResponseEntity.ok(notificationService.getNotifications(customUsers, page, size));
    }

    @PostMapping("/read/{notificationId}")
    @Operation(summary = "알림 읽기 API", description = "notificationId를 받아 해당 알림을 삭제합니다.")
    public ResponseEntity<String> readNotification(
            @AuthenticationPrincipal CustomUsers customUsers,
            @PathVariable Long notificationId
    ) {
        return ResponseEntity.ok(notificationService.readNotification(customUsers, notificationId));
    }

    @PostMapping("/readAll")
    @Operation(summary = "알림 모두 읽기 API", description = "로그인 된 유저의 모든 알림을 삭제합니다.")
    public ResponseEntity<String> readNotification(
            @AuthenticationPrincipal CustomUsers customUsers
    ) {
        return ResponseEntity.ok(notificationService.readAllNotifications(customUsers));
    }

}