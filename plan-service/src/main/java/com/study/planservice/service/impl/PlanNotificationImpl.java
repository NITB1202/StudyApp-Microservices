package com.study.planservice.service.impl;

import com.study.planservice.event.PlanEventPublisher;
import com.study.planservice.service.PlanNotificationService;
import com.study.common.events.Plan.*;
import com.study.planservice.service.PlanService;
import com.study.planservice.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlanNotificationImpl implements PlanNotificationService {
    private final PlanService planService;
    private final TaskService taskService;
    private final PlanEventPublisher publisher;

    private static final String REMIND_TOPIC = "plan-reminded";
    private static final String ASSIGN_TOPIC = "plan-assigned";
    private static final String COMPLETE_TOPIC = "plan-completed";
    private static final String DELETE_TOPIC = "plan-deleted";
    private static final String INCOMPLETE_TOPIC = "plan-incomplete";
    private static final String RESTORE_TOPIC = "plan-restored";
    private static final String UPDATE_TOPIC = "plan-updated";

    @Override
    public void publishPlanAssignedNotification(UUID planId, List<UUID> assigneeIds) {
        String planName = planService.getPlanName(planId);

        PlanAssignedEvent event = PlanAssignedEvent.builder()
                .planId(planId)
                .planName(planName)
                .assigneeIds(assigneeIds)
                .build();

        log.info("Publishing assign notification for plan {}", planId);
        publisher.publishEvent(ASSIGN_TOPIC, event);
    }

    @Override
    public void publishPlanCompletedNotification(UUID planId) {
        String planName = planService.getPlanName(planId);
        List<UUID> assigneeIds = taskService.getAllAssigneeForPlan(planId);

        PlanCompletedEvent event = PlanCompletedEvent.builder()
                .planId(planId)
                .planName(planName)
                .assigneeIds(assigneeIds)
                .build();

        log.info("Publishing complete notification for plan {}", planId);
        publisher.publishEvent(COMPLETE_TOPIC, event);
    }

    @Override
    public void publishPlanDeletedNotification(UUID userId, UUID planId) {
        String planName = planService.getPlanName(planId);
        List<UUID> assigneeIds = taskService.getAllAssigneeForPlan(planId);

        PlanDeletedEvent event = PlanDeletedEvent.builder()
                .userId(userId)
                .planName(planName)
                .assigneeIds(assigneeIds)
                .build();

        log.info("Publishing delete notification for plan {}", planName);
        publisher.publishEvent(DELETE_TOPIC, event);
    }

    @Override
    public void publishPlanIncompleteNotification(UUID userId, UUID planId) {
        String planName = planService.getPlanName(planId);
        List<UUID> assigneeIds = taskService.getAllAssigneeForPlan(planId);

        PlanIncompleteEvent event = PlanIncompleteEvent.builder()
                .userId(userId)
                .planId(planId)
                .planName(planName)
                .assigneeIds(assigneeIds)
                .build();

        log.info("Publishing incomplete notification for plan {}", planId);
        publisher.publishEvent(INCOMPLETE_TOPIC, event);
    }

    @Override
    public void publishPlanRemindedNotification(UUID planId, List<UUID> receiverIds) {
        String planName = planService.getPlanName(planId);
        LocalDateTime endAt = planService.getPlanEndAt(planId);

        PlanRemindedEvent event = PlanRemindedEvent.builder()
                .planId(planId)
                .planName(planName)
                .endAt(endAt)
                .receiverIds(receiverIds)
                .build();

        log.info("Publishing reminder notification for plan {}", planId);
        publisher.publishEvent(REMIND_TOPIC, event);
    }

    @Override
    public void publishPlanRestoredNotification(UUID userId, UUID planId) {
        String planName = planService.getPlanName(planId);
        List<UUID> assigneeIds = taskService.getAllAssigneeForPlan(planId);

        PlanRestoredEvent event = PlanRestoredEvent.builder()
                .userId(userId)
                .planId(planId)
                .planName(planName)
                .assigneeIds(assigneeIds)
                .build();

        log.info("Publishing restore notification for plan {}", planId);
        publisher.publishEvent(RESTORE_TOPIC, event);
    }

    @Override
    public void publishPlanUpdatedNotification(UUID userId, UUID planId, List<UUID> receiverIds) {
        String planName = planService.getPlanName(planId);

        PlanUpdatedEvent event = PlanUpdatedEvent.builder()
                .userId(userId)
                .planId(planId)
                .planName(planName)
                .assigneeIds(receiverIds)
                .build();

        log.info("Publishing update notification for plan {}", planId);
        publisher.publishEvent(UPDATE_TOPIC, event);
    }
}
