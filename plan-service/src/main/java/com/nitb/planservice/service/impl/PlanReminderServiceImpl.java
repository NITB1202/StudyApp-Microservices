package com.nitb.planservice.service.impl;

import com.nitb.planservice.entity.PlanReminder;
import com.nitb.planservice.job.PlanReminderJob;
import com.nitb.planservice.repository.PlanReminderRepository;
import com.nitb.planservice.service.PlanReminderService;
import com.nitb.planservice.service.PlanService;
import com.study.common.exceptions.BusinessException;
import com.study.common.exceptions.NotFoundException;
import com.study.planservice.grpc.*;
import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlanReminderServiceImpl implements PlanReminderService {
    private final PlanReminderRepository planReminderRepository;
    private final PlanService planService;
    private final Scheduler scheduler;

    @Override
    public void createPlanReminders(CreatePlanRemindersRequest request) {
        UUID planId = UUID.fromString(request.getPlanId());
        planService.validateRemindTimesList(planId, request.getRemindTimesList());

        List<PlanReminder> reminders = new ArrayList<>();

        for(String time : request.getRemindTimesList()) {
            LocalDateTime remindAt = LocalDateTime.parse(time);
            String receiverIds = String.join(",", request.getReceiverIdsList());

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

        for(UpdatePlanReminderRequest updateRequest : request.getRequestsList()) {
            LocalDateTime remindAt = LocalDateTime.parse(updateRequest.getRemindAt());
            UUID id = UUID.fromString(updateRequest.getId());

            if(planReminderRepository.existsByPlanIdAndRemindAt(planId, remindAt)){
                throw new BusinessException("This reminder has already existed in this plan.");
            }

            PlanReminder reminder = planReminderRepository.findById(id).orElseThrow(
                    ()-> new NotFoundException("Reminder with id " + id + " not found.")
            );

            if(reminder.getPlanId() != planId){
                throw new BusinessException("Reminder with id " + id + " does not belong to the plan");
            }

            reminder.setRemindAt(remindAt);
            planReminderRepository.save(reminder);

            cancelReminder(id);
            scheduleReminder(reminder);
        }
    }

    @Override
    public void deletePlanReminders(DeletePlanRemindersRequest request) {
        UUID planId = UUID.fromString(request.getPlanId());

        for(String idStr : request.getReminderIdsList()){
            UUID id = UUID.fromString(idStr);
            PlanReminder reminder = planReminderRepository.findById(id).orElseThrow(
                    ()-> new NotFoundException("Reminder with id " + id + " not found.")
            );

            if(reminder.getPlanId() != planId){
                throw new BusinessException("Reminder with id " + id + " does not belong to the plan");
            }

            planReminderRepository.delete(reminder);
            cancelReminder(id);
        }
    }

    @Override
    public void deleteAllByPlanId(UUID planId) {
        planReminderRepository.deleteAllByPlanId(planId);
    }

    @Override
    public void updateReceivers(UUID planId, List<UUID> receiverIds) {
        List<PlanReminder> reminders = planReminderRepository.findAllByPlanId(planId);
        String receiverIdsStr = String.join(",", receiverIds.stream().map(UUID::toString).toList());

        for(PlanReminder reminder : reminders) {
            reminder.setReceiverIds(receiverIdsStr);
            planReminderRepository.save(reminder);

            cancelReminder(reminder.getId());
            scheduleReminder(reminder);
        }
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
