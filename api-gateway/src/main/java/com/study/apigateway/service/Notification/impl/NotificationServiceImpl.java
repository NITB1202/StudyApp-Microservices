package com.study.apigateway.service.Notification.impl;

import com.study.apigateway.dto.Action.ActionResponseDto;
import com.study.apigateway.dto.Notification.response.NotificationsResponseDto;
import com.study.apigateway.dto.Notification.response.UnreadNotificationCountResponseDto;
import com.study.apigateway.grpc.NotificationGrpcClient;
import com.study.apigateway.service.Notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationGrpcClient notificationGrpcClient;

    @Override
    public NotificationsResponseDto getNotifications(UUID userId, LocalDateTime cursor, int size) {
        return null;
    }

    @Override
    public UnreadNotificationCountResponseDto getUnreadNotificationCount(UUID userId) {
        return null;
    }

    @Override
    public ActionResponseDto markNotificationsAsRead(List<UUID> ids) {
        return null;
    }

    @Override
    public ActionResponseDto markAllNotificationsAsRead(UUID userId) {
        return null;
    }

    @Override
    public ActionResponseDto deleteNotifications(List<UUID> ids) {
        return null;
    }

    @Override
    public ActionResponseDto deleteAllNotifications(UUID userId) {
        return null;
    }
}
