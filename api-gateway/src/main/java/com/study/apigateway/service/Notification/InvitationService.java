package com.study.apigateway.service.Notification;

import com.study.apigateway.dto.Action.ActionResponseDto;
import com.study.apigateway.dto.Notification.response.InvitationsResponseDto;

import java.time.LocalDateTime;
import java.util.UUID;

public interface InvitationService {
    InvitationsResponseDto getInvitations(UUID userId, LocalDateTime cursor, int size);
    ActionResponseDto replyToInvitation(UUID id, boolean accept);
}
