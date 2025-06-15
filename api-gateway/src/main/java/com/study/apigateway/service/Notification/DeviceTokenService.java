package com.study.apigateway.service.Notification;

import com.study.apigateway.dto.Action.ActionResponseDto;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface DeviceTokenService {
    Mono<ActionResponseDto> registerDeviceToken(UUID userId, String fmcToken);
    Mono<ActionResponseDto> removeDeviceToken(String fmcToken);
}
