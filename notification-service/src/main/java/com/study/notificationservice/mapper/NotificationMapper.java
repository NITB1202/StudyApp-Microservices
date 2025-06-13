package com.study.notificationservice.mapper;

import com.study.notificationservice.entity.Notification;
import com.study.common.mappers.LinkedSubjectMapper;
import com.study.notificationservice.grpc.NotificationResponse;
import com.study.notificationservice.grpc.NotificationsResponse;

import java.util.List;

public class NotificationMapper {
    private NotificationMapper() {}

    public static NotificationResponse toNotificationResponse(Notification notification) {
        String subjectId = notification.getSubjectId() != null ? notification.getSubjectId().toString() : "";

        return NotificationResponse.newBuilder()
                .setTitle(notification.getTitle())
                .setContent(notification.getContent())
                .setCreatedAt(notification.getCreatedAt().toString())
                .setIsRead(notification.getIsRead())
                .setSubject(LinkedSubjectMapper.toGrpcEnum(notification.getSubject()))
                .setSubjectId(subjectId)
                .build();
    }

    public static NotificationsResponse toNotificationsResponse(List<Notification> notifications, long total, String nextCursor) {
        List<NotificationResponse> notificationResponses = notifications.stream()
                .map(NotificationMapper::toNotificationResponse)
                .toList();

        return NotificationsResponse.newBuilder()
                .addAllNotifications(notificationResponses)
                .setTotal(total)
                .setNextCursor(nextCursor)
                .build();
    }
}
