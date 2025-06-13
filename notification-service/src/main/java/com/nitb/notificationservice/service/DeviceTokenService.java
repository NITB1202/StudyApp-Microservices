package com.nitb.notificationservice.service;

import com.nitb.notificationservice.dto.CreateNotificationDto;
import com.study.notificationservice.grpc.RegisterDeviceTokenRequest;

public interface DeviceTokenService {
    void registerDeviceToken(RegisterDeviceTokenRequest request);
    void sendPushNotification(CreateNotificationDto dto);
}
