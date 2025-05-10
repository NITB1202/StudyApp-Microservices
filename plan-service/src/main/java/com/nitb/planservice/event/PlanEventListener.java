package com.nitb.planservice.event;

import com.nitb.planservice.entity.Plan;
import com.nitb.planservice.service.PlanService;
import com.nitb.planservice.service.TaskService;
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

    @KafkaListener(topics = "team-deleted", groupId = "plan-service-group")
    public void consumeTeamDeletedEvent(TeamDeletedEvent event) {
        UUID teamId = event.getId();
        List<Plan> teamPlans = planService.getTeamPlans(teamId);

        for(Plan plan : teamPlans) {
            taskService.deleteAllByPlanId(plan.getId());
            planService.deletePlan(plan);
        }
    }
}
