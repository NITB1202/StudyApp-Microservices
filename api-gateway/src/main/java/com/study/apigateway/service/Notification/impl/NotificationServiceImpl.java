package com.study.apigateway.service.Notification.impl;

import com.study.apigateway.dto.Action.ActionResponseDto;
import com.study.apigateway.dto.Notification.response.NotificationsResponseDto;
import com.study.apigateway.dto.Notification.response.UnreadNotificationCountResponseDto;
import com.study.apigateway.grpc.NotificationGrpcClient;
import com.study.apigateway.mapper.ActionMapper;
import com.study.apigateway.mapper.NotificationMapper;
import com.study.apigateway.service.Notification.NotificationService;
import com.study.common.grpc.ActionResponse;
import com.study.notificationservice.grpc.GetUnreadNotificationCountResponse;
import com.study.notificationservice.grpc.NotificationsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationGrpcClient notificationGrpcClient;

    @Override
    public Mono<NotificationsResponseDto> getNotifications(UUID userId, LocalDateTime cursor, int size) {
        return Mono.fromCallable(()->{
            NotificationsResponse response = notificationGrpcClient.getNotifications(userId, cursor, size);
            return NotificationMapper.toNotificationsResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<UnreadNotificationCountResponseDto> getUnreadNotificationCount(UUID userId) {
        return Mono.fromCallable(()-> {
            GetUnreadNotificationCountResponse response = notificationGrpcClient.getUnreadNotificationCount(userId);
            return NotificationMapper.toUnreadNotificationCountResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<ActionResponseDto> markNotificationsAsRead(List<UUID> ids) {
        return Mono.fromCallable(()->{
            ActionResponse response = notificationGrpcClient.markNotificationAsRead(ids);
            return ActionMapper.toResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<ActionResponseDto> markAllNotificationsAsRead(UUID userId) {
        return Mono.fromCallable(()->{
            ActionResponse response = notificationGrpcClient.markAllNotificationsAsRead(userId);
            return ActionMapper.toResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<ActionResponseDto> deleteNotifications(UUID userId, List<UUID> ids) {
        return Mono.fromCallable(()->{
            ActionResponse response = notificationGrpcClient.deleteNotifications(userId, ids);
            return ActionMapper.toResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<ActionResponseDto> deleteAllNotifications(UUID userId) {
        return Mono.fromCallable(()->{
            ActionResponse response = notificationGrpcClient.deleteAllNotifications(userId);
            return ActionMapper.toResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
