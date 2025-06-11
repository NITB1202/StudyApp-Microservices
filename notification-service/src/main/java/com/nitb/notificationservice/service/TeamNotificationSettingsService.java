package com.nitb.notificationservice.service;

import com.nitb.notificationservice.entity.TeamNotificationSettings;
import com.study.notificationservice.grpc.GetTeamNotificationSettingsRequest;
import com.study.notificationservice.grpc.UpdateTeamNotificationSettingsRequest;

import java.util.UUID;

public interface TeamNotificationSettingsService {
    void createTeamNotificationSettings(UUID userId, UUID teamId);
    TeamNotificationSettings getTeamNotificationSettings(GetTeamNotificationSettingsRequest request);
    void updateTeamNotificationSettings(UpdateTeamNotificationSettingsRequest request);
    void deleteTeamNotificationSettings(UUID userId, UUID teamId);
    void deleteAllTeamNotificationSettings(UUID teamId);
}
