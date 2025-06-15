package com.study.apigateway.dto.Notification.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTeamNotificationSettingsRequestDto {
    private Boolean teamNotification;

    private Boolean teamPlanReminder;

    private Boolean chatNotification;
}
