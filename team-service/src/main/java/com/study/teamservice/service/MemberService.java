package com.study.teamservice.service;

import com.study.common.enums.TeamRole;
import com.study.common.exceptions.BusinessException;
import com.study.common.exceptions.NotFoundException;
import com.study.teamservice.entity.TeamUser;
import com.study.common.events.Notification.InvitationCreatedEvent;
import com.study.common.events.Notification.InvitationAcceptEvent;
import com.study.teamservice.event.TeamEventPublisher;
import com.study.common.events.Team.UserJoinedTeamEvent;
import com.study.teamservice.grpc.CreateInvitationRequest;
import com.study.teamservice.repository.TeamUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final TeamUserRepository teamUserRepository;
    private final TeamEventPublisher teamEventPublisher;

    private static final String INVITATION_CREATED_TOPIC = "invitation-created";
    private static final String INVITATION_ACCEPTED_TOPIC = "invitation-accepted";

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

        teamEventPublisher.publishEvent(INVITATION_CREATED_TOPIC, event);
    }

    @EventListener
    public void acceptInvitation(InvitationAcceptEvent request) {
        TeamUser teamUser = TeamUser.builder()
                .teamId(request.getTeamId())
                .userId(request.getUserId())
                .role(TeamRole.MEMBER)
                .joinDate(LocalDate.now())
                .build();

        teamUserRepository.save(teamUser);

        List<UUID> memberIds = getTeamMembersId(request.getTeamId());

        UserJoinedTeamEvent event = UserJoinedTeamEvent.builder()
                .teamId(request.getTeamId())
                .userId(request.getUserId())
                .memberIds(memberIds)
                .build();

        teamEventPublisher.publishEvent(INVITATION_ACCEPTED_TOPIC, event);
    }

    public List<UUID> getTeamMembersId(UUID teamId) {
        return teamUserRepository.findByTeamId(teamId).stream()
                .map(TeamUser::getUserId)
                .toList();
    }

}
