package com.nitb.notificationservice.job;

import com.nitb.notificationservice.service.DeviceTokenService;
import com.nitb.notificationservice.service.InvitationService;
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
    private final InvitationService invitationService;
    private final DeviceTokenService deviceTokenService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LocalDateTime now = LocalDateTime.now();
        //Delete notification after creating 1 week
        //now - createTime > 7days -> createTime < now - 7days
        LocalDateTime checkNotificationTime = now.minusDays(7);
        //Delete device token since last updated 1 month -> inactive in 1 month
        //now - lastUpdated > 1month -> lastUpdated < now - 1month
        LocalDateTime checkTokenTime = now.minusDays(30);

        notificationService.deleteNotificationBefore(checkNotificationTime);
        invitationService.deleteInvitationBefore(checkNotificationTime);
        deviceTokenService.deleteDeviceTokenBefore(checkTokenTime);
    }
}
