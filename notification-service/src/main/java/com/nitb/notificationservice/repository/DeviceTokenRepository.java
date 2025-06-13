package com.nitb.notificationservice.repository;

import com.nitb.notificationservice.entity.DeviceToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DeviceTokenRepository extends JpaRepository<DeviceToken, UUID> {
  DeviceToken findByFcmToken(String fcmToken);
  List<DeviceToken> findByUserId(UUID userId);
}