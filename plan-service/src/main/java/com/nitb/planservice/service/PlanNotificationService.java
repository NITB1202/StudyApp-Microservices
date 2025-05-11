package com.nitb.planservice.service;

import java.util.List;
import java.util.UUID;

public interface PlanNotificationService {
    void publishPlanRemindedNotification(UUID planId, List<UUID> receiverIds);
}
