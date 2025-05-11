package com.nitb.planservice.service;

import com.nitb.planservice.entity.PlanReminder;
import com.study.planservice.grpc.CreatePlanRemindersRequest;
import com.study.planservice.grpc.DeletePlanRemindersRequest;
import com.study.planservice.grpc.GetAllPlanRemindersInPlanRequest;
import com.study.planservice.grpc.UpdatePlanRemindersRequest;
import org.quartz.SchedulerException;

import java.util.List;
import java.util.UUID;

public interface PlanReminderService {
    void createPlanReminders(CreatePlanRemindersRequest request) throws SchedulerException;
    List<PlanReminder> getAllPlanRemindersInPlan(GetAllPlanRemindersInPlanRequest request);
    void updatePlanReminders(UpdatePlanRemindersRequest request) throws SchedulerException;
    void deletePlanReminders(DeletePlanRemindersRequest request) throws SchedulerException;

    void deleteAllByPlanId(UUID planId);
    void updateReceivers(UUID planId, List<UUID> receiverIds) throws SchedulerException;
}
