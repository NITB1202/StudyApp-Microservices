package com.study.planservice.service;

import com.study.planservice.entity.PlanReminder;
import com.study.planservice.grpc.CreatePlanRemindersRequest;
import com.study.planservice.grpc.DeletePlanRemindersRequest;
import com.study.planservice.grpc.GetAllPlanRemindersInPlanRequest;
import com.study.planservice.grpc.UpdatePlanRemindersRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface PlanReminderService {
    void createPlanReminders(List<UUID> planAssignees, CreatePlanRemindersRequest request);
    List<PlanReminder> getAllPlanRemindersInPlan(GetAllPlanRemindersInPlanRequest request);
    void updatePlanReminders(UpdatePlanRemindersRequest request);
    void deletePlanReminders(DeletePlanRemindersRequest request);

    void createPlanReminder(UUID planId, LocalDateTime remindAt, List<UUID> assigneeIds);
    void updateReceiversForAllPlanReminders(UUID planId, List<UUID> receiverIds);
    void updateRemindTime(UUID planId, LocalDateTime oldRemindAt, LocalDateTime newRemindAt);
    void deleteAllByPlanId(UUID planId);
}
