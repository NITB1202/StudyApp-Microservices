package com.nitb.notificationservice.service;

import com.nitb.notificationservice.dto.CreateNotificationRequestDto;
import com.nitb.notificationservice.entity.Notification;
import com.study.notificationservice.grpc.DeleteAllNotificationsRequest;
import com.study.notificationservice.grpc.DeleteNotificationsRequest;
import com.study.notificationservice.grpc.GetNotificationsRequest;
import com.study.notificationservice.grpc.MarkAllNotificationsAsReadRequest;

import java.util.List;

public interface NotificationService {
    void createNotification(CreateNotificationRequestDto request);
    List<Notification> getNotifications(GetNotificationsRequest request);
    void markNotificationAsRead(MarkAllNotificationsAsReadRequest request);
    void markAllNotificationsAsRead(MarkAllNotificationsAsReadRequest request);
    void deleteNotifications(DeleteNotificationsRequest request);
    void deleteAllNotifications(DeleteAllNotificationsRequest request);
}
