package com.study.apigateway.mapper;

import com.study.apigateway.dto.Notification.response.InvitationResponseDto;
import com.study.apigateway.dto.Notification.response.InvitationsResponseDto;
import com.study.notificationservice.grpc.InvitationResponse;
import com.study.notificationservice.grpc.InvitationsResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class InvitationMapper {
    private InvitationMapper() {}

    public static InvitationResponseDto toInvitationResponseDto(InvitationResponse invitation) {
        return InvitationResponseDto.builder()
                .id(UUID.fromString(invitation.getId()))
                .inviterName(invitation.getInviterName())
                .inviterAvatarUrl(invitation.getInviterAvatarUrl())
                .teamId(UUID.fromString(invitation.getTeamId()))
                .teamName(invitation.getTeamName())
                .invitedAt(LocalDateTime.parse(invitation.getInvitedAt()))
                .build();
    }

    public static InvitationsResponseDto toInvitationsResponseDto(InvitationsResponse invitations) {
        List<InvitationResponseDto> dto = invitations.getInvitationsList().stream()
                .map(InvitationMapper::toInvitationResponseDto)
                .toList();

        return InvitationsResponseDto.builder()
                .invitations(dto)
                .total(invitations.getTotal())
                .nextCursor(LocalDateTime.parse(invitations.getNextCursor()))
                .build();
    }
}
