package com.study.notificationservice.service;

import com.study.notificationservice.dto.CreateNotificationDto;
import com.study.notificationservice.entity.Notification;
import com.study.notificationservice.grpc.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface NotificationService {
    void createNotification(CreateNotificationDto request);
    List<Notification> getNotifications(GetNotificationsRequest request);
    int getUnreadNotificationCount(GetUnreadNotificationCountRequest request);
    void markNotificationAsRead(MarkNotificationsAsReadRequest request);
    void markAllNotificationsAsRead(MarkAllNotificationsAsReadRequest request);
    void deleteNotifications(DeleteNotificationsRequest request);
    void deleteAllNotifications(DeleteAllNotificationsRequest request);
    void deleteNotificationBefore(LocalDateTime dateTime);
    long countNotificationsByUserId(UUID userId);
}
