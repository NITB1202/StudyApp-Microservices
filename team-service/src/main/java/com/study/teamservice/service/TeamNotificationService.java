package com.study.teamservice.service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface TeamNotificationService {
    List<UUID> getTeamMembersId(UUID teamId);
    void publishTeamUpdateNotification(UUID userId, UUID teamId, Set<String> updatedFields);
    void publishTeamDeletionNotification(UUID userId, UUID teamId);
    void sentInvitation(UUID inviterId, UUID inviteeId, UUID teamId);
    void publishUserJoinedTeamNotification(UUID userId, UUID teamId);
    void publishUserLeftTeamNotification(UUID userId, UUID teamId);
}
