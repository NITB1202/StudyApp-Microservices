package com.nitb.notificationservice.repository;

import com.nitb.notificationservice.entity.TeamNotificationSettings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TeamNotificationSettingsRepository extends JpaRepository<TeamNotificationSettings, UUID> {
}