package com.nitb.notificationservice.mapper;

import com.nitb.notificationservice.entity.TeamNotificationSettings;
import com.study.notificationservice.grpc.TeamNotificationSettingsResponse;

public class TeamNotificationSettingsMapper {
    private TeamNotificationSettingsMapper() {}

    public static TeamNotificationSettingsResponse toTeamNotificationSettingsResponse(TeamNotificationSettings settings) {
        return TeamNotificationSettingsResponse.newBuilder()
                .setId(settings.getId().toString())
                .setTeamNotification(settings.getTeamNotification())
                .setTeamPlanReminder(settings.getTeamPlanReminder())
                .setChatNotification(settings.getChatNotification())
                .build();
    }
}
