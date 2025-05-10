package com.nitb.planservice.job;

import com.nitb.planservice.entity.Plan;
import com.nitb.planservice.repository.PlanRepository;
import com.nitb.planservice.repository.TaskRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PlanCleanupJob implements Job {
    private final PlanRepository planRepository;
    private final TaskRepository taskRepository;
    private final int EXPIRE_DAYS = 3;

    @Override
    @Transactional
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //Plan expire when EndOfDay(endAt + 3days) < now -> endAt < now - 3days
        LocalDateTime now = LocalDateTime.now();
        List<Plan> missedPlans = planRepository.findAllByCompleteAtNullAndEndAtBefore(now.minusDays(EXPIRE_DAYS));

        for (Plan plan : missedPlans) {
            LocalDateTime expireAt = (plan.getEndAt().plusDays(EXPIRE_DAYS)).with(LocalTime.MAX);
            if(expireAt.isBefore(now)) {
                taskRepository.deleteAllByPlanId(plan.getId());
                planRepository.delete(plan);
                log.info("Plan {} have been cleaned up", plan.getId());
            }
        }
    }
}
