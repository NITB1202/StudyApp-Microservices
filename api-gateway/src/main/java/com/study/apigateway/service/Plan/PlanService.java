package com.study.apigateway.service.Plan;

import com.study.apigateway.dto.Plan.Plan.request.CreatePersonalPlanRequestDto;
import com.study.apigateway.dto.Plan.Plan.request.CreateTeamPlanRequestDto;
import com.study.apigateway.dto.Plan.Plan.response.PlanResponseDto;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface PlanService {
    Mono<PlanResponseDto> createPersonalPlan(UUID userId, CreatePersonalPlanRequestDto request);
    Mono<PlanResponseDto> createTeamPlan(UUID userId, CreateTeamPlanRequestDto request);
}
