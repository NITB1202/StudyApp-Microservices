package com.study.teamservice.service.impl;

import com.study.common.events.Notification.InvitationCreatedEvent;
import com.study.common.events.Team.*;
import com.study.teamservice.event.TeamEventPublisher;
import com.study.teamservice.service.MemberService;
import com.study.teamservice.service.TeamNotificationService;
import com.study.teamservice.service.TeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamNotificationServiceImpl implements TeamNotificationService {
    private final TeamService teamService;
    private final MemberService memberService;
    private final TeamEventPublisher teamEventPublisher;

    private static final String CREATE_TOPIC = "team-created";
    private static final String UPDATE_TOPIC = "team-updated";
    private static final String DELETE_TOPIC = "team-deleted";

    private static final String INVITATION_CREATED_TOPIC = "invitation-created";
    private static final String USER_JOINED_TOPIC = "user-joined";
    private static final String USER_LEFT_TOPIC = "user-left";

    @Override
    public void publishTeamCreatedEvent(UUID teamId) {
        TeamCreatedEvent event = TeamCreatedEvent.builder()
                .teamId(teamId)
                .build();

        log.info("Publish team created event: {}", event);
        teamEventPublisher.publishEvent(CREATE_TOPIC, event);
    }

    @Override
    public void publishTeamUpdateNotification(UUID userId, UUID teamId, Set<String> updatedFields) {
        if(updatedFields.isEmpty()) {
            log.info("No fields updated for team {}. Skipping update notification.", teamId);
            return;
        }

        String teamName = teamService.getTeamName(teamId);
        List<UUID> memberIds = memberService.getTeamMemberIds(teamId);

        TeamUpdatedEvent event = TeamUpdatedEvent.builder()
                .id(teamId)
                .teamName(teamName)
                .updatedBy(userId)
                .updatedFields(updatedFields)
                .memberIds(memberIds)
                .build();

        log.info("Publishing team update notification for team {}: fields updated: {}", teamId, updatedFields);
        teamEventPublisher.publishEvent(UPDATE_TOPIC, event);
    }

    @Override
    public void publishTeamDeletionNotification(UUID userId, UUID teamId) {
        String teamName = teamService.getTeamName(teamId);
        List<UUID> memberIds = memberService.getTeamMemberIds(teamId);

        TeamDeletedEvent event = TeamDeletedEvent.builder()
                .id(teamId)
                .teamName(teamName)
                .deletedBy(userId)
                .memberIds(memberIds)
                .build();

        log.info("Publishing team deletion notification for team {}: deleted by user {}", teamId, userId);
        teamEventPublisher.publishEvent(DELETE_TOPIC, event);
    }

    @Override
    public void sentInvitation(UUID inviterId, UUID inviteeId, UUID teamId) {
        String teamName = teamService.getTeamName(teamId);

        InvitationCreatedEvent event = InvitationCreatedEvent.builder()
                .teamId(teamId)
                .teamName(teamName)
                .fromId(inviterId)
                .toId(inviteeId)
                .build();

        log.info("Sending invitation from user {} to user {} for team {}", inviterId, inviteeId, teamId);
        teamEventPublisher.publishEvent(INVITATION_CREATED_TOPIC, event);
    }

    @Override
    public void publishUserJoinedTeamNotification(UUID userId, UUID teamId) {
        String teamName = teamService.getTeamName(teamId);
        List<UUID> memberIds = memberService.getTeamMemberIds(teamId);

        UserJoinedTeamEvent event = UserJoinedTeamEvent.builder()
                .teamId(teamId)
                .teamName(teamName)
                .userId(userId)
                .memberIds(memberIds)
                .build();

        log.info("Publishing user joined team notification for user {}: team {}", userId, teamId);
        teamEventPublisher.publishEvent(USER_JOINED_TOPIC, event);
    }

    @Override
    public void publishUserLeftTeamNotification(UUID userId, UUID teamId){
        String teamName = teamService.getTeamName(teamId);
        List<UUID> memberIds = memberService.getTeamMemberIds(teamId);

        UserLeftTeamEvent event = UserLeftTeamEvent.builder()
                .teamId(teamId)
                .teamName(teamName)
                .userId(userId)
                .memberIds(memberIds)
                .build();

        log.info("Publishing user left team notification for user {}: team {}", userId, teamId);
        teamEventPublisher.publishEvent(USER_LEFT_TOPIC, event);
    }
}
