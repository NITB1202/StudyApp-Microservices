package com.study.teamservice.mapper;

import com.study.common.mappers.InvitationStatusMapper;
import com.study.teamservice.entity.Invitation;
import com.study.teamservice.grpc.InvitationResponse;

public class InvitationMapper {
    private InvitationMapper() {}

    public static InvitationResponse toInvitationResponse(Invitation invitation) {
        return InvitationResponse.newBuilder()
                .setId(invitation.getInviterId().toString())
                .setTeamId(invitation.getTeamId().toString())
                .setInviterId(invitation.getInviterId().toString())
                .setInviterId(invitation.getInviterId().toString())
                .setCreatedAt(invitation.getCreatedAt().toString())
                .setStatus(InvitationStatusMapper.toProtoEnum(invitation.getStatus()))
                .build();
    }
}
