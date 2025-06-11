package com.nitb.notificationservice.repository;

import com.nitb.notificationservice.entity.TeamNotificationSettings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TeamNotificationSettingsRepository extends JpaRepository<TeamNotificationSettings, UUID> {
    boolean existsByTeamIdAndUserId(UUID teamId, UUID userId);
    TeamNotificationSettings findByTeamIdAndUserId(UUID teamId, UUID userId);
    void deleteAllByTeamId(UUID teamId);
}