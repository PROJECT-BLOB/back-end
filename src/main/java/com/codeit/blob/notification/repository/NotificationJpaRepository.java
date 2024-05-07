package com.codeit.blob.notification.repository;

import com.codeit.blob.notification.domain.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationJpaRepository extends JpaRepository<Notification, Long> {

    Page<Notification> findAllByReceiverIdOrderByCreatedDateDesc(Long userId, Pageable pageable);

    void deleteAllByReceiverId(Long userId);
}
