package com.study.apigateway.service.Notification.impl;

import com.study.apigateway.dto.Notification.request.UpdateTeamNotificationSettingsRequestDto;
import com.study.apigateway.dto.Notification.response.TeamNotificationSettingsResponseDto;
import com.study.apigateway.grpc.NotificationGrpcClient;
import com.study.apigateway.mapper.TeamNotificationSettingsMapper;
import com.study.apigateway.service.Notification.TeamNotificationSettingsService;
import com.study.notificationservice.grpc.TeamNotificationSettingsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TeamNotificationSettingsServiceImpl implements TeamNotificationSettingsService {
    private final NotificationGrpcClient grpcClient;

    @Override
    public Mono<TeamNotificationSettingsResponseDto> getTeamNotificationSettings(UUID userId, UUID teamId) {
        return Mono.fromCallable(()->{
            TeamNotificationSettingsResponse response = grpcClient.getTeamNotificationSettings(teamId, userId);
            return TeamNotificationSettingsMapper.toTeamNotificationSettingsResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<TeamNotificationSettingsResponseDto> updateTeamNotificationSettings(UUID id, UpdateTeamNotificationSettingsRequestDto request) {
        return Mono.fromCallable(()->{
            TeamNotificationSettingsResponse response = grpcClient.updateTeamNotificationSettings(id, request);
            return TeamNotificationSettingsMapper.toTeamNotificationSettingsResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
