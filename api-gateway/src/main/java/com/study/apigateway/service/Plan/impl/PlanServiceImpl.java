package com.study.apigateway.service.Plan.impl;

import com.study.apigateway.dto.Action.ActionResponseDto;
import com.study.apigateway.dto.Plan.Plan.request.*;
import com.study.apigateway.dto.Plan.Plan.response.*;
import com.study.apigateway.dto.Plan.Task.request.CreateTaskRequestDto;
import com.study.apigateway.dto.Plan.Task.response.TaskResponseDto;
import com.study.apigateway.grpc.PlanServiceGrpcClient;
import com.study.apigateway.grpc.TeamServiceGrpcClient;
import com.study.apigateway.grpc.UserServiceGrpcClient;
import com.study.apigateway.mapper.ActionMapper;
import com.study.apigateway.mapper.PlanMapper;
import com.study.apigateway.mapper.TaskMapper;
import com.study.apigateway.service.Plan.PlanService;
import com.study.common.grpc.ActionResponse;
import com.study.planservice.grpc.*;
import com.study.userservice.grpc.UserDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService {
    private final TeamServiceGrpcClient teamServiceGrpc;
    private final PlanServiceGrpcClient planServiceGrpc;
    private final UserServiceGrpcClient userServiceGrpc;

    @Override
    public Mono<PlanResponseDto> createPersonalPlan(UUID userId, CreatePersonalPlanRequestDto request) {
        return Mono.fromCallable(()->{
            //Create plan
            CreatePlanRequestDto dto = CreatePlanRequestDto.builder()
                    .name(request.getName())
                    .description(request.getDescription())
                    .startAt(request.getStartAt())
                    .endAt(request.getEndAt())
                    .build();

            PlanResponse plan = planServiceGrpc.createPlan(userId, null, dto);
            UUID planId = UUID.fromString(plan.getId());

            //Create tasks
            List<CreateTaskRequestDto> tasksDto = new ArrayList<>();
            for(String task : request.getTasks() ) {
                CreateTaskRequestDto taskDto = TaskMapper.toCreateTaskRequestDto(task, userId);
                tasksDto.add(taskDto);
            }
            planServiceGrpc.createTasks(userId, planId, tasksDto);

            //Create reminders
            List<LocalDateTime> remindTimes = request.getRemindTimes();
            if(remindTimes != null && !remindTimes.isEmpty()) {
                planServiceGrpc.createPlanReminders(planId, remindTimes);
            }

            return PlanMapper.toPlanResponseDto(plan);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<PlanResponseDto> createTeamPlan(UUID userId, CreateTeamPlanRequestDto request) {
        return Mono.fromCallable(()->{
            //Create plan
            teamServiceGrpc.validateUpdateTeamResource(userId, request.getTeamId());

            CreatePlanRequestDto dto = CreatePlanRequestDto.builder()
                    .name(request.getName())
                    .description(request.getDescription())
                    .startAt(request.getStartAt())
                    .endAt(request.getEndAt())
                    .build();

            PlanResponse plan = planServiceGrpc.createPlan(userId, request.getTeamId(), dto);
            UUID planId = UUID.fromString(plan.getId());

            //Create tasks
            planServiceGrpc.createTasks(userId, planId, request.getTasks());

            //Create reminders
            List<LocalDateTime> remindTimes = request.getRemindTimes();
            if(remindTimes != null && !remindTimes.isEmpty()) {
                planServiceGrpc.createPlanReminders(planId, remindTimes);
            }

            return PlanMapper.toPlanResponseDto(plan);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<PlanDetailResponseDto> getPlanById(UUID planId) {
        return Mono.fromCallable(()->{
            PlanDetailResponse plan = planServiceGrpc.getPlanById(planId);
            PlanRemindersResponse reminders = planServiceGrpc.getAllPlanRemindersInPlan(planId);
            TasksResponse tasks = planServiceGrpc.getAllTasksInPlan(planId);

            List<TaskResponseDto> tasksDto = new ArrayList<>();
            for(TaskResponse task : tasks.getTasksList()) {
                UUID assigneeId = UUID.fromString(task.getAssigneeId());
                UserDetailResponse assignee = userServiceGrpc.getUserById(assigneeId);
                TaskResponseDto taskDto = TaskMapper.toTaskResponseDto(task, assignee);
                tasksDto.add(taskDto);
            }

            return PlanMapper.toPlanDetailResponseDto(plan, reminders, tasksDto);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<List<PlanSummaryResponseDto>> getAssignedPlansOnDate(UUID userId, LocalDate date) {
        return Mono.fromCallable(()->{
            PlansResponse plans = planServiceGrpc.getAssignedPlansOnDate(userId, date);
            return plans.getPlansList().stream().map(PlanMapper::toPlanSummaryResponseDto).toList();
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<List<TeamPlanSummaryResponseDto>> getTeamPlansOnDate(UUID userId, UUID teamId, LocalDate date) {
        return Mono.fromCallable(()->{
            TeamPlansResponse plans = planServiceGrpc.getTeamPlansOnDate(userId, teamId, date);
            return plans.getPlansList().stream().map(PlanMapper::toTeamPlanSummaryResponseDto).toList();
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<List<LocalDate>> getDatesWithAssignedPlanDeadlineInMonth(UUID userId, int month, int year) {
        return Mono.fromCallable(()->{
            DatesResponse response = planServiceGrpc.getDatesWithDeadlineInMonth(userId, month, year, null);
            return response.getDatesList().stream().map(LocalDate::parse).toList();
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<List<LocalDate>> getDatesWithTeamPlanDeadlineInMonth(UUID userId, UUID teamId, int month, int year) {
        return Mono.fromCallable(()->{
          DatesResponse response = planServiceGrpc.getDatesWithDeadlineInMonth(userId, month, year, teamId);
          return response.getDatesList().stream().map(LocalDate::parse).toList();
        });
    }

    @Override
    public Mono<List<PlanSummaryResponseDto>> getPersonalMissedPlans(UUID userId) {
        return Mono.fromCallable(()->{
            PlansResponse plans = planServiceGrpc.getPersonalMissedPlans(userId);
            return plans.getPlansList().stream().map(PlanMapper::toPlanSummaryResponseDto).toList();
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<List<TeamPlanSummaryResponseDto>> getTeamMissedPlans(UUID userId, UUID teamIdS) {
        return Mono.fromCallable(()->{
            TeamPlansResponse plans = planServiceGrpc.getTeamMissedPlans(userId, teamIdS);
            return plans.getPlansList().stream().map(PlanMapper::toTeamPlanSummaryResponseDto).toList();
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<PlanResponseDto> updatePlan(UUID userId, UUID planId, UpdatePlanRequestDto request) {
        return Mono.fromCallable(()->{
            validateIfUpdateTeamPlan(userId, planId);
            PlanResponse response = planServiceGrpc.updatePlan(userId, planId, request);
            return PlanMapper.toPlanResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<ActionResponseDto> deletePlan(UUID userId, UUID planId) {
        return Mono.fromCallable(()->{
            validateIfUpdateTeamPlan(userId, planId);
            ActionResponse response = planServiceGrpc.deletePlan(userId, planId);
            return ActionMapper.toResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<ActionResponseDto> restorePlan(UUID userId, UUID planId, RestorePlanRequestDto request) {
        return Mono.fromCallable(()->{
            validateIfUpdateTeamPlan(userId, planId);
            ActionResponse response = planServiceGrpc.restorePlan(userId, planId, request);
            return ActionMapper.toResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<PlanStatisticsResponseDto> getWeeklyPlanStatistics(UUID userId) {
        return Mono.fromCallable(()->{
            GetWeeklyPlanStatsResponse stats = planServiceGrpc.getWeeklyPlanStats(userId);
            return PlanMapper.toPlanStatisticsResponseDto(stats);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    private void validateIfUpdateTeamPlan(UUID userId, UUID planId) {
        ActionResponse response = planServiceGrpc.isTeamPlan(planId);

        if(response.getSuccess()) {
            UUID teamId = UUID.fromString(response.getMessage());
            teamServiceGrpc.validateUpdateTeamResource(userId, teamId);
        }
    }
}
