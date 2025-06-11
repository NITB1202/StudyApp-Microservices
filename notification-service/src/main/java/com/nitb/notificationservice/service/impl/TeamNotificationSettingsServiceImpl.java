package com.nitb.notificationservice.service.impl;

import com.nitb.notificationservice.entity.TeamNotificationSettings;
import com.nitb.notificationservice.repository.TeamNotificationSettingsRepository;
import com.nitb.notificationservice.service.TeamNotificationSettingsService;
import com.study.common.exceptions.NotFoundException;
import com.study.notificationservice.grpc.GetTeamNotificationSettingsRequest;
import com.study.notificationservice.grpc.UpdateTeamNotificationSettingsRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TeamNotificationSettingsServiceImpl implements TeamNotificationSettingsService {
    private final TeamNotificationSettingsRepository teamNotificationSettingsRepository;

    @Override
    public TeamNotificationSettings getTeamNotificationSettings(GetTeamNotificationSettingsRequest request) {
        UUID teamId = UUID.fromString(request.getTeamId());
        UUID userId = UUID.fromString(request.getUserId());

        TeamNotificationSettings settings = teamNotificationSettingsRepository.findByTeamIdAndUserId(teamId, userId);

        if(settings == null) {
            throw new NotFoundException("Incorrect user id or team id.");
        }

        return settings;
    }

    @Override
    public void updateTeamNotificationSettings(UpdateTeamNotificationSettingsRequest request) {
        UUID id = UUID.fromString(request.getId());

        TeamNotificationSettings settings = teamNotificationSettingsRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Incorrect user id or team id.")
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

        teamNotificationSettingsRepository.save(settings);
    }
}
