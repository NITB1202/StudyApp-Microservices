package com.nitb.notificationservice.job;

import com.nitb.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class NotificationCleanUpJob implements Job {
    private final NotificationService notificationService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //Delete notification after creating 1 week
        //now - createTime > 7days -> createTime < now - 7days
        LocalDateTime checkTime = LocalDateTime.now().minusDays(7);
        notificationService.deleteNotificationBefore(checkTime);
    }
}
