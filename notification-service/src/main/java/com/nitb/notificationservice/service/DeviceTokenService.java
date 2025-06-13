package com.nitb.notificationservice.service;

import com.nitb.notificationservice.dto.CreateNotificationDto;
import com.study.notificationservice.grpc.RegisterDeviceTokenRequest;
import com.study.notificationservice.grpc.RemoveDeviceTokenRequest;

import java.time.LocalDateTime;

public interface DeviceTokenService {
    void registerDeviceToken(RegisterDeviceTokenRequest request);
    void sendPushNotification(CreateNotificationDto dto);
    void removeDeviceToken(RemoveDeviceTokenRequest request);
    void deleteDeviceTokenBefore(LocalDateTime time);
}
