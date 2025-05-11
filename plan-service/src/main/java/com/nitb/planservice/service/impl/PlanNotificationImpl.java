package com.nitb.planservice.service.impl;

import com.nitb.planservice.event.PlanEventPublisher;
import com.nitb.planservice.service.PlanNotificationService;
import com.study.common.events.Plan.PlanRemindedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlanNotificationImpl implements PlanNotificationService {
    private final PlanEventPublisher publisher;
    private static final String REMIND_TOPIC = "plan-reminded";

    @Override
    public void publishPlanRemindedNotification(UUID planId, List<UUID> receiverIds) {
        PlanRemindedEvent event = PlanRemindedEvent.builder()
                .planId(planId)
                .receiverIds(receiverIds)
                .build();

        log.info("Publishing reminder notification for plan {}", planId);
        publisher.publishEvent(REMIND_TOPIC, event);
    }
}
