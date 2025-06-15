package com.study.apigateway.dto.Notification.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamNotificationSettingsResponseDto {
    private UUID id;

    private boolean teamNotification;

    private boolean teamPlanReminder;

    private boolean chatNotification;
}
