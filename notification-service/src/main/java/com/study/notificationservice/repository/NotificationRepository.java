package com.study.notificationservice.repository;

import com.study.notificationservice.entity.Notification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    List<Notification> findByUserId(UUID userId, Pageable pageable);
    List<Notification> findByUserIdAndCreatedAtLessThan(UUID userId, LocalDateTime createdAt, Pageable pageable);
    List<Notification> findByUserId(UUID userId);
    long countByUserId(UUID userId);
    int countByUserIdAndIsReadFalse(UUID userId);
    void deleteAllByCreatedAtBefore(LocalDateTime createdAt);
}