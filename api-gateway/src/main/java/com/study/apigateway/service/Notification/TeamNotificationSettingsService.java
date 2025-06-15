package com.study.apigateway.service.Notification;

import com.study.apigateway.dto.Notification.request.UpdateTeamNotificationSettingsRequestDto;
import com.study.apigateway.dto.Notification.response.TeamNotificationSettingsResponseDto;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface TeamNotificationSettingsService {
    Mono<TeamNotificationSettingsResponseDto> getTeamNotificationSettings(UUID userId, UUID teamId);
    Mono<TeamNotificationSettingsResponseDto> updateTeamNotificationSettings(UUID id, UpdateTeamNotificationSettingsRequestDto request);
}
