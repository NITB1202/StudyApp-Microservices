package com.study.apigateway.service.Plan.impl;

import com.study.apigateway.dto.Action.ActionResponseDto;
import com.study.apigateway.dto.Plan.Reminder.request.AddPlanRemindersRequestDto;
import com.study.apigateway.dto.Plan.Reminder.request.DeletePlanRemindersRequestDto;
import com.study.apigateway.dto.Plan.Reminder.request.UpdatePlanRemindersRequestDto;
import com.study.apigateway.grpc.PlanServiceGrpcClient;
import com.study.apigateway.grpc.TeamServiceGrpcClient;
import com.study.apigateway.mapper.ActionMapper;
import com.study.apigateway.service.Plan.PlanReminderService;
import com.study.common.grpc.ActionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlanReminderServiceImpl implements PlanReminderService {
    private final PlanServiceGrpcClient planGrpc;
    private final TeamServiceGrpcClient teamGrpc;

    @Override
    public Mono<ActionResponseDto> addPlanReminders(UUID userId, UUID planId, AddPlanRemindersRequestDto request) {
        return Mono.fromCallable(()->{
            validateIfUpdateTeamPlan(userId, planId);
            ActionResponse response = planGrpc.createPlanReminders(planId, request.getReminders());
            return ActionMapper.toResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<ActionResponseDto> updatePlanReminders(UUID userId, UUID planId, UpdatePlanRemindersRequestDto request) {
        return Mono.fromCallable(()->{
            validateIfUpdateTeamPlan(userId, planId);
            ActionResponse response = planGrpc.updatePlanReminders(planId, request);
            return ActionMapper.toResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<ActionResponseDto> deletePlanReminders(UUID userId, UUID planId, DeletePlanRemindersRequestDto request) {
        return Mono.fromCallable(()->{
            validateIfUpdateTeamPlan(userId, planId);
            ActionResponse response = planGrpc.deletePlanReminders(planId, request);
            return ActionMapper.toResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    private void validateIfUpdateTeamPlan(UUID userId, UUID planId) {
        ActionResponse isTeamPlan = planGrpc.isTeamPlan(planId);

        if(isTeamPlan.getSuccess()) {
            UUID teamId = UUID.fromString(isTeamPlan.getMessage());
            teamGrpc.validateUpdateTeamResource(userId, teamId);
        }
    }
}
