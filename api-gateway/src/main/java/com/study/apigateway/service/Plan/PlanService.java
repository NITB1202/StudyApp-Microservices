package com.study.apigateway.service.Plan;

import com.study.apigateway.dto.Action.ActionResponseDto;
import com.study.apigateway.dto.Plan.Plan.request.CreatePersonalPlanRequestDto;
import com.study.apigateway.dto.Plan.Plan.request.CreateTeamPlanRequestDto;
import com.study.apigateway.dto.Plan.Plan.request.RestorePlanRequestDto;
import com.study.apigateway.dto.Plan.Plan.request.UpdatePlanRequestDto;
import com.study.apigateway.dto.Plan.Plan.response.PlanSummaryResponseDto;
import com.study.apigateway.dto.Plan.Plan.response.PlanDetailResponseDto;
import com.study.apigateway.dto.Plan.Plan.response.PlanResponseDto;
import com.study.apigateway.dto.Plan.Plan.response.TeamPlanSummaryResponseDto;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface PlanService {
    Mono<PlanResponseDto> createPersonalPlan(UUID userId, CreatePersonalPlanRequestDto request);
    Mono<PlanResponseDto> createTeamPlan(UUID userId, CreateTeamPlanRequestDto request);
    Mono<PlanDetailResponseDto> getPlanById(UUID planId);
    Mono<List<PlanSummaryResponseDto>> getAssignedPlansOnDate(UUID userId, LocalDate date);
    Mono<List<TeamPlanSummaryResponseDto>> getTeamPlansOnDate(UUID userId, UUID teamId, LocalDate date);
    Mono<List<LocalDate>> getDatesWithAssignedPlanDeadlineInMonth(UUID userId, int month, int year);
    Mono<List<LocalDate>> getDatesWithTeamPlanDeadlineInMonth(UUID userId, UUID teamId, int month, int year);
    Mono<List<PlanSummaryResponseDto>> getPersonalMissedPlans(UUID userId);
    Mono<List<TeamPlanSummaryResponseDto>> getTeamMissedPlans(UUID userId, UUID teamIdS);
    Mono<PlanResponseDto> updatePlan(UUID userId, UUID planId, UpdatePlanRequestDto request);
    Mono<ActionResponseDto> deletePlan(UUID userId, UUID planId);
    Mono<ActionResponseDto> restorePlan(UUID userId, UUID planId, RestorePlanRequestDto request);
}
