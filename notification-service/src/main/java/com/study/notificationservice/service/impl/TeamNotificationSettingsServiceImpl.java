package com.study.notificationservice.service.impl;

import com.study.common.exceptions.NotFoundException;
import com.study.notificationservice.entity.TeamNotificationSettings;
import com.study.notificationservice.grpc.GetTeamNotificationSettingsRequest;
import com.study.notificationservice.grpc.UpdateTeamNotificationSettingsRequest;
import com.study.notificationservice.repository.TeamNotificationSettingsRepository;
import com.study.notificationservice.service.TeamNotificationSettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TeamNotificationSettingsServiceImpl implements TeamNotificationSettingsService {
    private final TeamNotificationSettingsRepository settingsRepository;

    @Override
    public void createTeamNotificationSettings(UUID userId, UUID teamId) {
        TeamNotificationSettings settings = TeamNotificationSettings.builder()
                .userId(userId)
                .teamId(teamId)
                .teamNotification(true)
                .teamPlanReminder(true)
                .chatNotification(true)
                .build();

        settingsRepository.save(settings);
    }

    @Override
    public TeamNotificationSettings getTeamNotificationSettings(GetTeamNotificationSettingsRequest request) {
        UUID teamId = UUID.fromString(request.getTeamId());
        UUID userId = UUID.fromString(request.getUserId());

        TeamNotificationSettings settings = settingsRepository.findByTeamIdAndUserId(teamId, userId);

        if(settings == null) {
            throw new NotFoundException("Team id or user id is incorrect.");
        }

        return settings;
    }

    @Override
    public void updateTeamNotificationSettings(UpdateTeamNotificationSettingsRequest request) {
        UUID id = UUID.fromString(request.getId());
        TeamNotificationSettings settings = settingsRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Team id or user id is incorrect.")
        );

        if(request.hasTeamNotification()) {
            settings.setTeamNotification(request.getTeamNotification().getValue());
        }

        if(request.hasTeamPlanReminder()) {
            settings.setTeamPlanReminder(request.getTeamPlanReminder().getValue());
        }

        if(request.hasChatNotification()) {
            settings.setChatNotification(request.getChatNotification().getValue());
        }

        settingsRepository.save(settings);
    }
}
