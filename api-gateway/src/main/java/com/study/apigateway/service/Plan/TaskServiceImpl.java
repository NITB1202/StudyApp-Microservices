package com.study.apigateway.service.Plan;

import com.study.apigateway.dto.Action.ActionResponseDto;
import com.study.apigateway.dto.Plan.Task.request.*;
import com.study.apigateway.grpc.PlanServiceGrpcClient;
import com.study.apigateway.grpc.TeamServiceGrpcClient;
import com.study.apigateway.mapper.ActionMapper;
import com.study.common.exceptions.BusinessException;
import com.study.common.grpc.ActionResponse;
import com.study.planservice.grpc.PlanDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final PlanServiceGrpcClient planGrpc;
    private final TeamServiceGrpcClient teamGrpc;

    @Override
    public Mono<ActionResponseDto> addTasksToPersonalPlan(UUID userId, AddTasksToPersonalPlanRequestDto request) {
        return Mono.fromCallable(()->{
            List<CreateTaskRequestDto> tasks = request.getTasks().stream()
                    .map(t -> CreateTaskRequestDto.builder()
                            .name(t)
                            .assigneeId(userId)
                            .build()
                    )
                    .toList();

            ActionResponse response = planGrpc.createTasks(userId, request.getPlanId(), tasks);
            return ActionMapper.toResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<ActionResponseDto> addTasksToTeamPlan(UUID userId, AddTasksToTeamPlanRequestDto request) {
        return Mono.fromCallable(()->{
            //Validate update team request
            PlanDetailResponse planDetail = planGrpc.getPlanById(request.getPlanId());
            UUID teamId = UUID.fromString(planDetail.getTeamId());
            teamGrpc.validateUpdateTeamResource(userId, teamId);

            //Validate task assignees
            Set<UUID> assigneeIds = new LinkedHashSet<>();
            for(CreateTaskRequestDto task : request.getTasks()) {
                assigneeIds.add(task.getAssigneeId());
            }
            teamGrpc.validateUsersInTeam(userId, assigneeIds);

            ActionResponse response = planGrpc.createTasks(userId, request.getPlanId(), request.getTasks());
            return ActionMapper.toResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<ActionResponseDto> updateTasksStatus(UUID userId, UpdateTasksStatusRequestDto request) {
        return Mono.fromCallable(()->{
            ActionResponse response = planGrpc.updateTasksStatus(userId, request);
            return ActionMapper.toResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<ActionResponseDto> updateTasksAssignee(UUID userId, UpdateTasksAssigneeRequestDto request) {
        return Mono.fromCallable(()->{
            //Validate update team request
            PlanDetailResponse planDetail = planGrpc.getPlanById(request.getPlanId());
            if(planDetail.getTeamId().isEmpty()){
                throw new BusinessException("Can't update task assignee for personal plan.");
            }

            UUID teamId = UUID.fromString(planDetail.getTeamId());
            teamGrpc.validateUpdateTeamResource(userId, teamId);

            ActionResponse response = planGrpc.updateTasksAssignee(userId, request);
            return ActionMapper.toResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<ActionResponseDto> deleteTasks(UUID userId, DeleteTasksRequestDto request) {
        return Mono.fromCallable(()->{
            PlanDetailResponse planDetail = planGrpc.getPlanById(request.getPlanId());

            //Validate update team request
            if(!planDetail.getTeamId().isEmpty()){
                UUID teamId = UUID.fromString(planDetail.getTeamId());
                teamGrpc.validateUpdateTeamResource(userId, teamId);
            }

            ActionResponse response = planGrpc.deleteTasks(userId, request);
            return ActionMapper.toResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
