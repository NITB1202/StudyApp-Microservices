package com.study.apigateway.mapper;

import com.study.apigateway.dto.Notification.response.TeamNotificationSettingsResponseDto;
import com.study.notificationservice.grpc.TeamNotificationSettingsResponse;

import java.util.UUID;

public class TeamNotificationSettingsMapper {
    private TeamNotificationSettingsMapper() {}

    public static TeamNotificationSettingsResponseDto toTeamNotificationSettingsResponseDto(TeamNotificationSettingsResponse settings) {
        return TeamNotificationSettingsResponseDto.builder()
                .id(UUID.fromString(settings.getId()))
                .teamNotification(settings.getTeamNotification())
                .teamPlanReminder(settings.getTeamPlanReminder())
                .chatNotification(settings.getChatNotification())
                .build();
    }
}
