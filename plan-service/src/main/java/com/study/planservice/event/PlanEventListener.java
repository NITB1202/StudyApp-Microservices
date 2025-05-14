package com.study.planservice.event;

import com.study.planservice.entity.Plan;
import com.study.planservice.service.PlanReminderService;
import com.study.planservice.service.PlanService;
import com.study.planservice.service.TaskService;
import com.study.common.events.Team.TeamDeletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlanEventListener {
    private final PlanService planService;
    private final TaskService taskService;
    private final PlanReminderService planReminderService;

    @KafkaListener(topics = "team-deleted", groupId = "plan-service-group")
    public void consumeTeamDeletedEvent(TeamDeletedEvent event) {
        UUID teamId = event.getId();
        List<Plan> teamPlans = planService.getAllTeamPlans(teamId);

        for(Plan plan : teamPlans) {
            taskService.deleteAllByPlanId(plan.getId());
            planReminderService.deleteAllByPlanId(plan.getId());
            planService.delete(plan);
        }
    }
}
