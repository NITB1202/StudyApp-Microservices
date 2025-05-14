package com.study.planservice.job;

import com.study.planservice.service.PlanNotificationService;
import com.study.planservice.service.PlanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class PlanReminderJob implements Job {
    private final PlanService planService;
    private final PlanNotificationService planNotificationService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap dataMap = context.getMergedJobDataMap();

        UUID planId = UUID.fromString(dataMap.getString("planId"));

        //If plan is completed, doesn't need to send notification
        if(planService.getPlanProgress(planId) == 1f) return;

        String receiverIdsStr = dataMap.getString("receiverIds");
        List<UUID> receiverIds = Arrays.stream(receiverIdsStr.split(","))
                .map(UUID::fromString)
                .toList();
        String planName = dataMap.getString("planName");
        LocalDateTime endAt = LocalDateTime.parse(dataMap.getString("endAt"));

        planNotificationService.publishPlanRemindedNotification(planId, planName, endAt, receiverIds);
    }
}
