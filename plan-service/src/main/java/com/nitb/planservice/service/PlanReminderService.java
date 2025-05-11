package com.nitb.planservice.service;

import com.nitb.planservice.entity.PlanReminder;
import com.study.planservice.grpc.CreatePlanRemindersRequest;
import com.study.planservice.grpc.DeletePlanRemindersRequest;
import com.study.planservice.grpc.GetAllPlanRemindersInPlanRequest;
import com.study.planservice.grpc.UpdatePlanRemindersRequest;

import java.util.List;
import java.util.UUID;

public interface PlanReminderService {
    void createPlanReminders(CreatePlanRemindersRequest request);
    List<PlanReminder> getAllPlanRemindersInPlan(GetAllPlanRemindersInPlanRequest request);
    void updatePlanReminders(UpdatePlanRemindersRequest request);
    void deletePlanReminders(DeletePlanRemindersRequest request);

    void deleteAllByPlanId(UUID planId);
    void updateReceivers(UUID planId, List<UUID> receiverIds);
}
