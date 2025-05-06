package com.study.teamservice.service.impl;

import com.study.common.events.Notification.InvitationCreatedEvent;
import com.study.common.events.Team.TeamDeletedEvent;
import com.study.common.events.Team.TeamUpdatedEvent;
import com.study.common.events.Team.UserJoinedTeamEvent;
import com.study.common.events.Team.UserLeftTeamEvent;
import com.study.teamservice.entity.TeamUser;
import com.study.teamservice.event.TeamEventPublisher;
import com.study.teamservice.repository.TeamUserRepository;
import com.study.teamservice.service.TeamNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TeamNotificationServiceImpl implements TeamNotificationService {
    private final TeamUserRepository teamUserRepository;
    private final TeamEventPublisher teamEventPublisher;

    private static final String UPDATE_TOPIC = "team-updated";
    private static final String DELETE_TOPIC = "team-deleted";

    private static final String INVITATION_CREATED_TOPIC = "invitation-created";
    private static final String USER_JOINED_TOPIC = "user-joined";
    private static final String USER_LEFT_TOPIC = "user-left";

    @Override
    public List<UUID> getTeamMembersId(UUID teamId) {
        return teamUserRepository.findByTeamId(teamId).stream()
                .map(TeamUser::getUserId)
                .toList();
    }

    @Override
    public void publishTeamUpdateNotification(UUID userId, UUID teamId, Set<String> updatedFields) {
        if(updatedFields.isEmpty()) return;

        List<UUID> memberIds = getTeamMembersId(teamId);

        TeamUpdatedEvent event = TeamUpdatedEvent.builder()
                .id(teamId)
                .updatedBy(userId)
                .updatedFields(updatedFields)
                .memberIds(memberIds)
                .build();

        teamEventPublisher.publishEvent(UPDATE_TOPIC, event);
    }

    @Override
    public void publishTeamDeletionNotification(UUID userId, UUID teamId) {
        List<UUID> memberIds = getTeamMembersId(teamId);

        TeamDeletedEvent event = TeamDeletedEvent.builder()
                .id(teamId)
                .deletedBy(userId)
                .memberIds(memberIds)
                .build();

        teamEventPublisher.publishEvent(DELETE_TOPIC, event);
    }

    @Override
    public void sentInvitation(UUID inviterId, UUID inviteeId, UUID teamId) {
        InvitationCreatedEvent event = InvitationCreatedEvent.builder()
                .teamId(teamId)
                .fromId(inviterId)
                .toId(inviteeId)
                .build();

        teamEventPublisher.publishEvent(INVITATION_CREATED_TOPIC, event);
    }

    @Override
    public void publishUserJoinedTeamNotification(UUID userId, UUID teamId) {
        List<UUID> memberIds = getTeamMembersId(teamId);

        UserJoinedTeamEvent event = UserJoinedTeamEvent.builder()
                .teamId(teamId)
                .userId(userId)
                .memberIds(memberIds)
                .build();

        teamEventPublisher.publishEvent(USER_JOINED_TOPIC, event);
    }

    @Override
    public void publishUserLeftTeamNotification(UUID userId, UUID teamId){
        List<UUID> memberIds = getTeamMembersId(teamId);

        UserLeftTeamEvent event = UserLeftTeamEvent.builder()
                .teamId(teamId)
                .userId(userId)
                .memberIds(memberIds)
                .build();

        teamEventPublisher.publishEvent(USER_LEFT_TOPIC, event);
    }
}
