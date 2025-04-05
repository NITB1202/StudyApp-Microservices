package com.study.teamservice.service;

import com.study.common.exceptions.BusinessException;
import com.study.common.exceptions.NotFoundException;
import com.study.teamservice.entity.TeamUser;
import com.study.teamservice.event.InvitationCreatedEvent;
import com.study.teamservice.event.TeamEventPublisher;
import com.study.teamservice.grpc.CreateInvitationRequest;
import com.study.teamservice.grpc.ReplyToInvitationRequest;
import com.study.teamservice.repository.TeamUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final TeamUserRepository teamUserRepository;
    private static final String INVITATION_TOPIC = "invitation-created";
    private final TeamEventPublisher teamEventPublisher;

    public void createInvitation(CreateInvitationRequest request) {
        UUID teamId = UUID.fromString(request.getTeamId());
        UUID inviterId = UUID.fromString(request.getInviterId());
        UUID inviteeId = UUID.fromString(request.getInviteeId());

        if(!teamUserRepository.existsById(teamId)) {
            throw new BusinessException("Team does not exist");
        }

        if(!teamUserRepository.existsByUserIdAndTeamId(inviterId, teamId)) {
            throw new NotFoundException("User is not in this team");
        }

        InvitationCreatedEvent event = InvitationCreatedEvent.builder()
                .teamId(teamId)
                .fromId(inviterId)
                .toId(inviteeId)
                .build();

        teamEventPublisher.publishEvent(INVITATION_TOPIC, event);
    }

    public void replyToInvitation(ReplyToInvitationRequest request) {

    }

    public List<UUID> getTeamMembersId(UUID teamId) {
        return teamUserRepository.findByTeamId(teamId).stream()
                .map(TeamUser::getUserId)
                .toList();
    }

}
