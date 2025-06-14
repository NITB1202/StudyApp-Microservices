package com.study.apigateway.service.Notification;

import com.study.apigateway.dto.Action.ActionResponseDto;

import java.util.UUID;

public interface DeviceTokenService {
    ActionResponseDto registerDeviceToken(UUID userId, String fmcToken);
    ActionResponseDto removeDeviceToken(String fmcToken);
}
