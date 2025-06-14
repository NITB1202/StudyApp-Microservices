package com.study.apigateway.service.Notification.impl;

import com.study.apigateway.dto.Action.ActionResponseDto;
import com.study.apigateway.grpc.NotificationGrpcClient;
import com.study.apigateway.service.Notification.DeviceTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeviceTokenServiceImpl implements DeviceTokenService {
    private final NotificationGrpcClient notificationGrpcClient;

    @Override
    public ActionResponseDto registerDeviceToken(UUID userId, String fmcToken) {
        return null;
    }

    @Override
    public ActionResponseDto removeDeviceToken(String fmcToken) {
        return null;
    }
}
