package com.nitb.planservice.mapper;

import com.nitb.planservice.entity.Plan;
import com.study.planservice.grpc.*;

import java.util.Arrays;
import java.util.List;

public class PlanMapper {
    private PlanMapper() {}

    public static PlanResponse toPlanResponse(Plan plan) {
        List<String> remindList = Arrays.asList(plan.getRemindAt().split(","));

        return PlanResponse.newBuilder()
                .setId(plan.getId().toString())
                .setName(plan.getName())
                .setDescription(plan.getDescription())
                .setStartAt(plan.getStartAt().toString())
                .setEndAt(plan.getEndAt().toString())
                .addAllRemindAt(remindList)
                .build();
    }

    public static PlanDetailResponse toPlanDetailResponse(Plan plan) {
        List<String> remindList = Arrays.asList(plan.getRemindAt().split(","));
        String completeAt = plan.getCompleteAt() == null ? "" : plan.getCompleteAt().toString();
        String teamId = plan.getTeamId() == null ? "" : plan.getTeamId().toString();

        return PlanDetailResponse.newBuilder()
                .setId(plan.getId().toString())
                .setCreatorId(plan.getCreatorId().toString())
                .setName(plan.getName())
                .setDescription(plan.getDescription())
                .setStartAt(plan.getStartAt().toString())
                .setEndAt(plan.getEndAt().toString())
                .addAllRemindAt(remindList)
                .setProgress(plan.getProgress())
                .setCompleteAt(completeAt)
                .setTeamId(teamId)
                .build();
    }

    public static PlanSummaryResponse toPlanSummaryResponse(Plan plan) {
        return PlanSummaryResponse.newBuilder()
                .setId(plan.getId().toString())
                .setName(plan.getName())
                .setProgress(plan.getProgress())
                .setEndAt(plan.getEndAt().toString())
                .build();
    }

    public static PlansResponse toPlansResponse(List<Plan> plans) {
        List<PlanSummaryResponse> plansResponse = plans.stream()
                .map(PlanMapper::toPlanSummaryResponse)
                .toList();

        return PlansResponse.newBuilder()
                .addAllPlans(plansResponse)
                .build();
    }

    public static TeamPlanSummaryResponse toTeamPlanSummaryResponse(Plan plan, boolean isAssigned) {
        return TeamPlanSummaryResponse.newBuilder()
                .setId(plan.getId().toString())
                .setName(plan.getName())
                .setProgress(plan.getProgress())
                .setEndAt(plan.getEndAt().toString())
                .setIsAssigned(isAssigned)
                .build();
    }

    public static TeamPlansResponse toTeamPlansResponse(List<TeamPlanSummaryResponse> plans) {
        return TeamPlansResponse.newBuilder()
                .addAllPlans(plans)
                .build();
    }
}
