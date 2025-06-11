package com.nitb.notificationservice.service.impl;

import com.nitb.notificationservice.dto.CreateNotificationRequestDto;
import com.nitb.notificationservice.entity.Notification;
import com.nitb.notificationservice.repository.NotificationRepository;
import com.nitb.notificationservice.service.NotificationService;
import com.study.notificationservice.grpc.DeleteAllNotificationsRequest;
import com.study.notificationservice.grpc.DeleteNotificationsRequest;
import com.study.notificationservice.grpc.GetNotificationsRequest;
import com.study.notificationservice.grpc.MarkAllNotificationsAsReadRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;

    @Override
    public void createNotification(CreateNotificationRequestDto request) {

    }

    @Override
    public List<Notification> getNotifications(GetNotificationsRequest request) {
        return List.of();
    }

    @Override
    public void markNotificationAsRead(MarkAllNotificationsAsReadRequest request) {

    }

    @Override
    public void markAllNotificationsAsRead(MarkAllNotificationsAsReadRequest request) {

    }

    @Override
    public void deleteNotifications(DeleteNotificationsRequest request) {

    }

    @Override
    public void deleteAllNotifications(DeleteAllNotificationsRequest request) {

    }
}
