package com.study.notificationservice.mapper;

import com.study.notificationservice.entity.Invitation;
import com.study.notificationservice.grpc.InvitationResponse;
import com.study.notificationservice.grpc.InvitationsResponse;

import java.util.List;

public class InvitationMapper {
    private InvitationMapper() {}

    public static InvitationResponse toInvitationResponse(Invitation invitation) {
        return InvitationResponse.newBuilder()
                .setId(invitation.getId().toString())
                .setInviterName(invitation.getInviterName())
                .setTeamId(invitation.getTeamId().toString())
                .setTeamName(invitation.getTeamName())
                .setInvitedAt(invitation.getInvitedAt().toString())
                .build();
    }

    public static InvitationsResponse toInvitationsResponse(List<Invitation> invitations, long total, String nextCursor) {
        List<InvitationResponse> invitationResponses = invitations.stream()
                .map(InvitationMapper::toInvitationResponse)
                .toList();

        return InvitationsResponse.newBuilder()
                .addAllInvitations(invitationResponses)
                .setTotal(total)
                .setNextCursor(nextCursor)
                .build();
    }
}
