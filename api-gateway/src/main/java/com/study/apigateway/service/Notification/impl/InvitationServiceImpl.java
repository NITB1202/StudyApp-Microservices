package com.study.apigateway.service.Notification.impl;

import com.study.apigateway.dto.Action.ActionResponseDto;
import com.study.apigateway.dto.Notification.response.InvitationsResponseDto;
import com.study.apigateway.grpc.NotificationGrpcClient;
import com.study.apigateway.service.Notification.InvitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvitationServiceImpl implements InvitationService {
    private final NotificationGrpcClient notificationGrpcClient;

    @Override
    public InvitationsResponseDto getInvitations(UUID userId, LocalDateTime cursor, int size) {
        return null;
    }

    @Override
    public ActionResponseDto replyToInvitation(UUID id, boolean accept) {
        return null;
    }
}
