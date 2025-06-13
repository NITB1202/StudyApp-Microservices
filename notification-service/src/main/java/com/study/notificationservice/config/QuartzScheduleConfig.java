package com.study.notificationservice.config;

import com.study.notificationservice.job.NotificationCleanUpJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzScheduleConfig {
    @Bean
    public JobDetail cleanupJobDetail() {
        return JobBuilder.newJob(NotificationCleanUpJob.class)
                .withIdentity("notificationCleanupJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger cleanupTrigger() {
        return TriggerBuilder.newTrigger()
                .forJob(cleanupJobDetail())
                .withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(0, 0))
                .build();
    }
}