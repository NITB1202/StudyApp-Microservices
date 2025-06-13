package com.nitb.notificationservice.service.impl;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.nitb.notificationservice.dto.CreateNotificationDto;
import com.nitb.notificationservice.entity.DeviceToken;
import com.nitb.notificationservice.repository.DeviceTokenRepository;
import com.nitb.notificationservice.service.DeviceTokenService;
import com.study.common.exceptions.BusinessException;
import com.study.notificationservice.grpc.RegisterDeviceTokenRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeviceTokenServiceImpl implements DeviceTokenService {
    private final DeviceTokenRepository deviceTokenRepository;

    @Override
    public void registerDeviceToken(RegisterDeviceTokenRequest request) {
        UUID userId = UUID.fromString(request.getUserId());

        DeviceToken token = deviceTokenRepository.findByFcmToken(request.getFcmToken());

        if (token == null) {
            token = DeviceToken.builder()
                    .userId(userId)
                    .fcmToken(request.getFcmToken())
                    .build();
        }

        token.setLastUpdated(LocalDateTime.now());

        deviceTokenRepository.save(token);
    }

    @Override
    public void sendPushNotification(CreateNotificationDto dto) {
        try {
            List<DeviceToken> tokens = deviceTokenRepository.findByUserId(dto.getUserId());

            Map<String, String> data = new HashMap<>();
            data.put("type", dto.getSubject().toString());
            data.put("id", dto.getSubjectId().toString());

            Notification notification = Notification.builder()
                    .setTitle(dto.getTitle())
                    .setBody(dto.getContent())
                    .build();

            for (DeviceToken token : tokens) {
                Message message = Message.builder()
                        .setToken(token.getFcmToken())
                        .setNotification(notification)
                        .putAllData(data)
                        .build();

                FirebaseMessaging.getInstance().send(message);
            }

        } catch (FirebaseMessagingException e) {
            throw new BusinessException("Error sending FCM message: " + e.getMessage());
        }
    }
}
