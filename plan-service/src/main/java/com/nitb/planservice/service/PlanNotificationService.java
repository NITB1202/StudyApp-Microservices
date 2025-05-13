package com.nitb.planservice.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface PlanNotificationService {
    void publishPlanAssignedNotification(UUID planId, String planName, List<UUID> assigneeIds);
    void publishPlanCompletedNotification(UUID planId, String planName, List<UUID> assigneeIds);
    void publishPlanDeletedNotification(UUID userId, String planName, List<UUID> assigneeIds);
    void publishPlanIncompleteNotification(UUID userId, UUID planId, String planName, List<UUID> assigneeIds);
    void publishPlanRemindedNotification(UUID planId, String planName, LocalDateTime endAt, List<UUID> receiverIds);
    void publishPlanRestoredNotification(UUID userId, UUID planId, String planName, List<UUID> assigneeIds);
    void publishPlanUpdatedNotification(UUID userId, UUID planId, String planName, List<UUID> assigneeIds);
}
