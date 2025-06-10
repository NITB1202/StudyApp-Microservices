package com.nitb.notificationservice.service;

import com.nitb.notificationservice.entity.TeamNotificationSettings;
import com.study.notificationservice.grpc.GetTeamNotificationSettingsRequest;
import com.study.notificationservice.grpc.UpdateTeamNotificationSettingsRequest;

public interface TeamNotificationSettingsService {
    TeamNotificationSettings getTeamNotificationSettings(GetTeamNotificationSettingsRequest request);
    void updateTeamNotificationSettings(UpdateTeamNotificationSettingsRequest request);
}
