package com.study.notificationservice.repository;

import com.study.notificationservice.entity.TeamNotificationSettings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TeamNotificationSettingsRepository extends JpaRepository<TeamNotificationSettings, UUID> {
    TeamNotificationSettings findByTeamIdAndUserId(UUID teamId, UUID userId);
    void deleteAllByTeamId(UUID teamId);
}