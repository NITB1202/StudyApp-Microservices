package com.nitb.notificationservice.service.impl;

import com.nitb.notificationservice.entity.TeamNotificationSettings;
import com.nitb.notificationservice.repository.TeamNotificationSettingsRepository;
import com.nitb.notificationservice.service.TeamNotificationSettingsService;
import com.study.notificationservice.grpc.GetTeamNotificationSettingsRequest;
import com.study.notificationservice.grpc.UpdateTeamNotificationSettingsRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamNotificationSettingsServiceImpl implements TeamNotificationSettingsService {
    private final TeamNotificationSettingsRepository teamNotificationSettingsRepository;

    @Override
    public TeamNotificationSettings getTeamNotificationSettings(GetTeamNotificationSettingsRequest request) {
        return null;
    }

    @Override
    public void updateTeamNotificationSettings(UpdateTeamNotificationSettingsRequest request) {

    }
}
