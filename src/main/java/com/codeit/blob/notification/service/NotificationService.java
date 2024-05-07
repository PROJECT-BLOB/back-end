package com.codeit.blob.notification.service;

import com.codeit.blob.comment.domain.Comment;
import com.codeit.blob.global.exceptions.CustomException;
import com.codeit.blob.global.exceptions.ErrorCode;
import com.codeit.blob.notification.domain.Notification;
import com.codeit.blob.notification.repository.NotificationJpaRepository;
import com.codeit.blob.notification.response.NotificationPageResponse;
import com.codeit.blob.oauth.domain.CustomUsers;
import com.codeit.blob.post.domain.PostLike;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationJpaRepository notificationJpaRepository;

    @Transactional
    public void createPostLikeNotification(PostLike like){
        // 본인일 경우 알림 보내지 않기
        if (like.getUser().getId().equals(like.getPost().getAuthor().getId())) return;

        Notification notification = new Notification(like);
        notificationJpaRepository.save(notification);
    }

    @Transactional
    public void createCommentNotification(Comment comment){
        // 본인일 경우 알림 보내지 않기
        if ( (comment.getParent() == null
                && comment.getAuthor().getId().equals(comment.getPost().getAuthor().getId()))
                || (comment.getParent() != null
                && comment.getAuthor().getId().equals(comment.getParent().getAuthor().getId())))
            return;

        Notification notification = new Notification(comment);
        notificationJpaRepository.save(notification);
    }

    @Transactional
    public String readNotification(CustomUsers userDetail, Long notificationId){
        Notification notification = notificationJpaRepository.findById(notificationId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOTIFICATION_NOT_FOUND));

        if (!notification.getReceiver().getId().equals(userDetail.getUsers().getId())){
            throw new CustomException(ErrorCode.ACTION_ACCESS_DENIED);
        }

        notificationJpaRepository.deleteById(notificationId);
        return "알림 삭제 성공";
    }

    @Transactional
    public String readAllNotifications(CustomUsers userDetail){
        notificationJpaRepository.deleteAllByReceiverId(userDetail.getUsers().getId());
        return "알림 삭제 성공";
    }

    @Transactional(readOnly=true)
    public NotificationPageResponse getNotifications(CustomUsers userDetail, int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<Notification> notifications = notificationJpaRepository
                .findAllByReceiverIdOrderByCreatedDateDesc(userDetail.getUsers().getId(), pageable);
        return new NotificationPageResponse(notifications);
    }

}
