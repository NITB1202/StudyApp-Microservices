package com.study.apigateway.service.Plan.impl;

import com.study.apigateway.dto.Action.ActionResponseDto;
import com.study.apigateway.dto.Plan.Reminder.request.AddPlanRemindersRequestDto;
import com.study.apigateway.dto.Plan.Reminder.request.DeletePlanRemindersRequestDto;
import com.study.apigateway.dto.Plan.Reminder.request.UpdatePlanRemindersRequestDto;
import com.study.apigateway.grpc.PlanServiceGrpcClient;
import com.study.apigateway.mapper.ActionMapper;
import com.study.apigateway.service.Plan.PlanReminderService;
import com.study.common.grpc.ActionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
public class PlanReminderServiceImpl implements PlanReminderService {
    private final PlanServiceGrpcClient planGrpc;

    @Override
    public Mono<ActionResponseDto> addPlanReminders(AddPlanRemindersRequestDto request) {
        return Mono.fromCallable(()->{
            ActionResponse response = planGrpc.createPlanReminders(request.getPlanId(), request.getReminders());
            return ActionMapper.toResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<ActionResponseDto> updatePlanReminders(UpdatePlanRemindersRequestDto request) {
        return Mono.fromCallable(()->{
            ActionResponse response = planGrpc.updatePlanReminders(request);
            return ActionMapper.toResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<ActionResponseDto> deletePlanReminders(DeletePlanRemindersRequestDto request) {
        return Mono.fromCallable(()->{
            ActionResponse response = planGrpc.deletePlanReminders(request);
            return ActionMapper.toResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
