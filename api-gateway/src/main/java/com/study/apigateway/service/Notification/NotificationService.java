package com.study.apigateway.service.Notification;

import com.study.apigateway.dto.Action.ActionResponseDto;
import com.study.apigateway.dto.Notification.response.NotificationsResponseDto;
import com.study.apigateway.dto.Notification.response.UnreadNotificationCountResponseDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface NotificationService {
    NotificationsResponseDto getNotifications(UUID userId, LocalDateTime cursor, int size);
    UnreadNotificationCountResponseDto getUnreadNotificationCount(UUID userId);
    ActionResponseDto markNotificationsAsRead(List<UUID> ids);
    ActionResponseDto markAllNotificationsAsRead(UUID userId);
    ActionResponseDto deleteNotifications(List<UUID> ids);
    ActionResponseDto deleteAllNotifications(UUID userId);
}
