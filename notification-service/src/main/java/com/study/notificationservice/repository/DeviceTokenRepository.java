package com.study.notificationservice.repository;

import com.study.notificationservice.entity.DeviceToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface DeviceTokenRepository extends JpaRepository<DeviceToken, UUID> {
  DeviceToken findByFcmToken(String fcmToken);
  List<DeviceToken> findByUserId(UUID userId);
  void deleteByFcmToken(String fcmToken);
  void deleteAllByLastUpdatedBefore(LocalDateTime time);
}