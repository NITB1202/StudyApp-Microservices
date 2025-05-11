package com.nitb.planservice.job;

import com.nitb.planservice.entity.Plan;
import com.nitb.planservice.service.PlanReminderService;
import com.nitb.planservice.service.PlanService;
import com.nitb.planservice.service.TaskService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PlanCleanupJob implements Job {
    private final PlanService planService;
    private final PlanReminderService planReminderService;
    private final TaskService taskService;

    @Override
    @Transactional
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        List<Plan> expiredPlans = planService.getAllExpiredPlans();

        for (Plan plan : expiredPlans) {
            taskService.deleteAllByPlanId(plan.getId());
            planReminderService.deleteAllByPlanId(plan.getId());
            planService.delete(plan);
            log.info("Plan {} have been cleaned up", plan.getId());
        }
    }
}
