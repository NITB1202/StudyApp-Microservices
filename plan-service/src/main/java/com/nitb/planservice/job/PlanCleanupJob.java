package com.nitb.planservice.job;

import com.nitb.planservice.entity.Plan;
import com.nitb.planservice.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PlanCleanupJob {
    private final PlanRepository planRepository;
    private final int EXPIRE_DAYS = 3;

    @Scheduled(cron = "0 0 0 * * *")
    public void deleteExpiredPlans() {
        LocalDateTime now = LocalDateTime.now();
        List<Plan> plans = planRepository.findAll();

        for (Plan plan : plans) {
            LocalDateTime expireAt = (plan.getEndAt().plusDays(EXPIRE_DAYS)).with(LocalTime.MAX);
            if(expireAt.isBefore(now)) {
                planRepository.delete(plan);
                log.info("Plan {} have been cleaned up", plan.getId());
            }
        }
    }
}
