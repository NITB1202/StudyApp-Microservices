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
        return NotificationResponseDto.builder()
                .id(UUID.fromString(notification.getId()))
                .title(notification.getTitle())
                .content(notification.getContent())
                .createdAt(LocalDateTime.parse(notification.getCreatedAt()))
                .isRead(notification.getIsRead())
                .subject(LinkedSubjectMapper.toEnum(notification.getSubject()))
                .subjectId(UUID.fromString(notification.getSubjectId()))
                .build();
    }

    public static NotificationsResponseDto toNotificationsResponseDto(NotificationsResponse notifications) {
        List<NotificationResponseDto> dto = notifications.getNotificationsList().stream()
                .map(NotificationMapper::toNotificationResponseDto)
                .toList();

        return NotificationsResponseDto.builder()
                .notifications(dto)
                .total(notifications.getTotal())
                .nextCursor(LocalDateTime.parse(notifications.getNextCursor()))
                .build();
    }

    public static UnreadNotificationCountResponseDto toUnreadNotificationCountResponseDto(GetUnreadNotificationCountResponse count) {
        return UnreadNotificationCountResponseDto.builder()
                .count(count.getCount())
                .build();
    }
}
