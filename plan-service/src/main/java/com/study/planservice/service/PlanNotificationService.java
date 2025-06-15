package com.study.planservice.service;

import java.util.List;
import java.util.UUID;

public interface PlanNotificationService {
    void publishPlanAssignedNotification(UUID planId, List<UUID> assigneeIds);
    void publishPlanCompletedNotification(UUID planId);
    void publishPlanDeletedNotification(UUID userId, UUID planId);
    void publishPlanIncompleteNotification(UUID userId, UUID planId);
    void publishPlanRemindedNotification(UUID planId, UUID teamId, List<UUID> receiverIds);
    void publishPlanRestoredNotification(UUID userId, UUID planId);
    void publishPlanUpdatedNotification(UUID userId, UUID planId, List<UUID> receiverIds);
}
