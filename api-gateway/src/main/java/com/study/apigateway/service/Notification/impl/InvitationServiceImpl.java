package com.study.apigateway.service.Notification.impl;

import com.study.apigateway.dto.Action.ActionResponseDto;
import com.study.apigateway.dto.Notification.response.InvitationsResponseDto;
import com.study.apigateway.grpc.NotificationServiceGrpcClient;
import com.study.apigateway.mapper.ActionMapper;
import com.study.apigateway.mapper.InvitationMapper;
import com.study.apigateway.service.Notification.InvitationService;
import com.study.common.grpc.ActionResponse;
import com.study.notificationservice.grpc.InvitationsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvitationServiceImpl implements InvitationService {
    private final NotificationServiceGrpcClient notificationGrpcClient;

    @Override
    public Mono<InvitationsResponseDto> getInvitations(UUID userId, LocalDateTime cursor, int size) {
        return Mono.fromCallable(()->{
            InvitationsResponse response = notificationGrpcClient.getInvitations(userId, cursor, size);
            return InvitationMapper.toInvitationsResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<ActionResponseDto> replyToInvitation(UUID id, UUID userId, boolean accept) {
        return Mono.fromCallable(()->{
            ActionResponse response = notificationGrpcClient.replyToInvitation(id, userId, accept);
            return ActionMapper.toResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
