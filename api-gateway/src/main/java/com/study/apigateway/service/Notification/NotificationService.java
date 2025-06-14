package com.study.apigateway.service.Notification;

import com.study.apigateway.dto.Action.ActionResponseDto;
import com.study.apigateway.dto.Notification.response.NotificationsResponseDto;
import com.study.apigateway.dto.Notification.response.UnreadNotificationCountResponseDto;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface NotificationService {
    Mono<NotificationsResponseDto> getNotifications(UUID userId, LocalDateTime cursor, int size);
    Mono<UnreadNotificationCountResponseDto> getUnreadNotificationCount(UUID userId);
    Mono<ActionResponseDto> markNotificationsAsRead(List<UUID> ids);
    Mono<ActionResponseDto> markAllNotificationsAsRead(UUID userId);
    Mono<ActionResponseDto> deleteNotifications(List<UUID> ids);
    Mono<ActionResponseDto> deleteAllNotifications(UUID userId);
}
