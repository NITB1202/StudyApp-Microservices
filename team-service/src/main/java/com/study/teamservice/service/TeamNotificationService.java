package com.study.teamservice.service;

import com.study.teamservice.entity.Team;

import java.util.Set;
import java.util.UUID;

public interface TeamNotificationService {
    void publishTeamCreatedEvent(UUID teamId);
    void publishTeamUpdateNotification(UUID userId, UUID teamId, Set<String> updatedFields);
    void publishTeamDeletionNotification(UUID userId, UUID teamId);
    void sentInvitation(UUID inviterId, UUID inviteeId, UUID teamId);
    void publishUserJoinedTeamNotification(UUID userId, UUID teamId);
    void publishUserLeftTeamNotification(UUID userId, UUID teamId);
}
