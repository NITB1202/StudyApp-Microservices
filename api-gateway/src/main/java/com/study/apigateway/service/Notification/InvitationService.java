package com.study.apigateway.service.Notification;

import com.study.apigateway.dto.Action.ActionResponseDto;
import com.study.apigateway.dto.Notification.response.InvitationsResponseDto;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

public interface InvitationService {
    Mono<InvitationsResponseDto> getInvitations(UUID userId, LocalDateTime cursor, int size);
    Mono<ActionResponseDto> replyToInvitation(UUID id, UUID userId, boolean accept);
}
