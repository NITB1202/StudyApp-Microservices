package com.study.apigateway.service.Notification;

import com.study.apigateway.dto.Action.ActionResponseDto;
import com.study.apigateway.dto.Notification.request.RegisterDeviceTokenRequestDto;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface DeviceTokenService {
    Mono<ActionResponseDto> registerDeviceToken(UUID userId, RegisterDeviceTokenRequestDto request);
    Mono<ActionResponseDto> removeDeviceToken(String fmcToken);
}
