package com.study.teamservice.service;

import java.util.UUID;

public interface TeamNotificationService {
    void publishTeamCreatedEvent(UUID teamId, UUID creatorId);
    void publishTeamUpdateNotification(UUID userId, UUID teamId);
    void publishTeamDeletionNotification(UUID userId, UUID teamId);
    void sentInvitation(UUID inviterId, UUID inviteeId, UUID teamId);
    void publishUserJoinedTeamNotification(UUID userId, UUID teamId);
    void publishUserLeftTeamNotification(UUID userId, UUID teamId);
}
