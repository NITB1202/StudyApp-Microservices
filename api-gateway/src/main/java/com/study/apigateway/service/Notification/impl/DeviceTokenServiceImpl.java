package com.study.apigateway.service.Notification.impl;

import com.study.apigateway.dto.Action.ActionResponseDto;
import com.study.apigateway.dto.Notification.request.RegisterDeviceTokenRequestDto;
import com.study.apigateway.grpc.NotificationGrpcClient;
import com.study.apigateway.mapper.ActionMapper;
import com.study.apigateway.service.Notification.DeviceTokenService;
import com.study.common.grpc.ActionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeviceTokenServiceImpl implements DeviceTokenService {
    private final NotificationGrpcClient notificationGrpcClient;

    @Override
    public Mono<ActionResponseDto> registerDeviceToken(UUID userId, RegisterDeviceTokenRequestDto request) {
        return Mono.fromCallable(()->{
            ActionResponse response = notificationGrpcClient.registerDeviceToken(userId, request.getFcmToken());
            return ActionMapper.toResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<ActionResponseDto> removeDeviceToken(String fmcToken) {
        return Mono.fromCallable(()->{
            ActionResponse response = notificationGrpcClient.removeDeviceToken(fmcToken);
            return ActionMapper.toResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
