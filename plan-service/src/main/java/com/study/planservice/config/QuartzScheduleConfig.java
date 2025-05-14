package com.study.planservice.config;

import com.study.planservice.job.PlanCleanupJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzScheduleConfig {
    @Bean
    public JobDetail cleanupJobDetail() {
        return JobBuilder.newJob(PlanCleanupJob.class)
                .withIdentity("planCleanupJob")
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
