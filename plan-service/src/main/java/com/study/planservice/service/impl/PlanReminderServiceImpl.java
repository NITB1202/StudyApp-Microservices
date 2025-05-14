package com.study.planservice.service.impl;

import com.study.planservice.entity.PlanReminder;
import com.study.planservice.job.PlanReminderJob;
import com.study.planservice.repository.PlanReminderRepository;
import com.study.planservice.service.PlanReminderService;
import com.study.planservice.service.PlanService;
import com.study.common.exceptions.BusinessException;
import com.study.common.exceptions.NotFoundException;
import com.study.planservice.grpc.*;
import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PlanReminderServiceImpl implements PlanReminderService {
    private final PlanReminderRepository planReminderRepository;
    private final PlanService planService;
    private final Scheduler scheduler;

    @Override
    public void createPlanReminders(List<UUID> planAssignees, CreatePlanRemindersRequest request) {
        UUID planId = UUID.fromString(request.getPlanId());
        String receiverIds = String.join(",", planAssignees.stream().map(UUID::toString).toList());

        planService.validateRemindTimesList(planId, request.getRemindTimesList());

        Set<LocalDateTime> times = new LinkedHashSet<>();
        List<PlanReminder> reminders = new ArrayList<>();

        for(String time : request.getRemindTimesList()) {
            LocalDateTime remindAt = LocalDateTime.parse(time);

            if(!times.add(remindAt) || planReminderRepository.existsByPlanIdAndRemindAt(planId, remindAt)) {
                throw new BusinessException("Duplicated reminder " + time);
            }

            PlanReminder reminder = PlanReminder.builder()
                    .planId(planId)
                    .receiverIds(receiverIds)
                    .remindAt(remindAt)
                    .build();

            reminders.add(reminder);
            scheduleReminder(reminder);
        }

        planReminderRepository.saveAll(reminders);
    }

    @Override
    public List<PlanReminder> getAllPlanRemindersInPlan(GetAllPlanRemindersInPlanRequest request) {
        UUID planId = UUID.fromString(request.getPlanId());
        return planReminderRepository.findAllByPlanId(planId);
    }

    @Override
    public void updatePlanReminders(UpdatePlanRemindersRequest request) {
        UUID planId = UUID.fromString(request.getPlanId());
        List<String> updatedTime = request.getRequestsList().stream()
                .map(UpdatePlanReminderRequest::getRemindAt)
                .toList();

        planService.validateRemindTimesList(planId, updatedTime);

        List<PlanReminder> reminders = new ArrayList<>();

        for(UpdatePlanReminderRequest updateRequest : request.getRequestsList()) {
            UUID id = UUID.fromString(updateRequest.getId());
            LocalDateTime remindAt = LocalDateTime.parse(updateRequest.getRemindAt());

            if(planReminderRepository.existsByPlanIdAndRemindAt(planId, remindAt)){
                throw new BusinessException("Reminder " + remindAt + " has already existed in this plan.");
            }

            PlanReminder reminder = planReminderRepository.findById(id).orElseThrow(
                    ()-> new NotFoundException("Reminder with id " + id + " not found.")
            );

            if(!reminder.getPlanId().equals(planId)){
                throw new BusinessException("Reminder with id " + id + " does not belong to the plan");
            }

            reminder.setRemindAt(remindAt);
            reminders.add(reminder);

            cancelReminder(id);
            scheduleReminder(reminder);
        }

        planReminderRepository.saveAll(reminders);
    }

    @Override
    public void deletePlanReminders(DeletePlanRemindersRequest request) {
        UUID planId = UUID.fromString(request.getPlanId());

        planService.validateUpdatePlanRequest(planId);

        List<PlanReminder> deletedReminders = new ArrayList<>();

        for(String idStr : request.getReminderIdsList()){
            UUID id = UUID.fromString(idStr);

            PlanReminder reminder = planReminderRepository.findById(id).orElseThrow(
                    ()-> new NotFoundException("Reminder with id " + id + " not found.")
            );

            if(!reminder.getPlanId().equals(planId)){
                throw new BusinessException("Reminder with id " + id + " does not belong to the plan");
            }

            deletedReminders.add(reminder);
            cancelReminder(id);
        }

        planReminderRepository.deleteAll(deletedReminders);
    }

    @Override
    public void createPlanReminder(UUID planId, LocalDateTime remindAt, List<UUID> assigneeIds) {
        planService.validateRemindTimesList(planId, List.of(remindAt.toString()));
        String idsStr = String.join(",", assigneeIds.stream().map(UUID::toString).toList());

        PlanReminder reminder = PlanReminder.builder()
                .planId(planId)
                .receiverIds(idsStr)
                .remindAt(remindAt)
                .build();

        planReminderRepository.save(reminder);
        scheduleReminder(reminder);
    }

    @Override
    public void deleteAllByPlanId(UUID planId) {
        planReminderRepository.deleteAllByPlanId(planId);
    }

    @Override
    public void updateReceiversForAllPlanReminders(UUID planId, List<UUID> receiverIds) {
        List<PlanReminder> reminders = planReminderRepository.findAllByPlanId(planId);
        String receiverIdsStr = String.join(",", receiverIds.stream().map(UUID::toString).toList());

        List<PlanReminder> updatedReminders = new ArrayList<>();

        for(PlanReminder reminder : reminders) {
            reminder.setReceiverIds(receiverIdsStr);
            updatedReminders.add(reminder);

            cancelReminder(reminder.getId());
            scheduleReminder(reminder);
        }

        planReminderRepository.saveAll(updatedReminders);
    }

    @Override
    public void updateRemindTime(UUID planId, LocalDateTime oldRemindAt, LocalDateTime newRemindAt) {
        PlanReminder reminder = planReminderRepository.findByPlanIdAndRemindAt(planId, oldRemindAt);

        if(reminder == null){
            throw new NotFoundException("Reminder not found.");
        }

        reminder.setRemindAt(newRemindAt);
        planReminderRepository.save(reminder);

        cancelReminder(reminder.getId());
        scheduleReminder(reminder);
    }

    private void scheduleReminder(PlanReminder reminder) {
        JobDetail jobDetail = JobBuilder.newJob(PlanReminderJob.class)
                .withIdentity("reminder_" + reminder.getId())
                .usingJobData("planId", reminder.getPlanId().toString())
                .usingJobData("receiverIds", reminder.getReceiverIds())
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("trigger_" + reminder.getId())
                .startAt(Timestamp.valueOf(reminder.getRemindAt()))
                .build();

        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            throw new BusinessException(e.getMessage());
        }
    }

    private void cancelReminder(UUID reminderId) {
        try {
            JobKey key = new JobKey("reminder_" + reminderId);
            if (scheduler.checkExists(key)) {
                scheduler.deleteJob(key);
            }
        } catch (SchedulerException e) {
            throw new BusinessException(e.getMessage());
        }
    }
}
