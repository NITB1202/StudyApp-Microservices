package com.study.apigateway.mapper;

import com.study.apigateway.dto.Notification.response.NotificationResponseDto;
import com.study.apigateway.dto.Notification.response.NotificationsResponseDto;
import com.study.apigateway.dto.Notification.response.UnreadNotificationCountResponseDto;
import com.study.common.mappers.LinkedSubjectMapper;
import com.study.notificationservice.grpc.GetUnreadNotificationCountResponse;
import com.study.notificationservice.grpc.NotificationResponse;
import com.study.notificationservice.grpc.NotificationsResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class NotificationMapper {
    private NotificationMapper() {}

    public static NotificationResponseDto toNotificationResponseDto(NotificationResponse notification) {
        UUID subjectId = notification.getSubjectId().isEmpty()? null: UUID.fromString(notification.getSubjectId());

        return NotificationResponseDto.builder()
                .id(UUID.fromString(notification.getId()))
                .title(notification.getTitle())
                .content(notification.getContent())
                .createdAt(LocalDateTime.parse(notification.getCreatedAt()))
                .isRead(notification.getIsRead())
                .subject(LinkedSubjectMapper.toEnum(notification.getSubject()))
                .subjectId(subjectId)
                .build();
    }

    public static NotificationsResponseDto toNotificationsResponseDto(NotificationsResponse notifications) {
        LocalDateTime nextCursor = notifications.getNextCursor().isEmpty()? null: LocalDateTime.parse(notifications.getNextCursor());
        List<NotificationResponseDto> dto = notifications.getNotificationsList().stream()
                .map(NotificationMapper::toNotificationResponseDto)
                .toList();

        return NotificationsResponseDto.builder()
                .notifications(dto)
                .total(notifications.getTotal())
                .nextCursor(nextCursor)
                .build();
    }

    public static UnreadNotificationCountResponseDto toUnreadNotificationCountResponseDto(GetUnreadNotificationCountResponse count) {
        return UnreadNotificationCountResponseDto.builder()
                .count(count.getCount())
                .build();
    }
}
