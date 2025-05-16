package com.study.apigateway.service.Plan;

import com.study.apigateway.dto.Action.ActionResponseDto;
import com.study.apigateway.dto.Plan.Task.request.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface TaskService {
    Mono<ActionResponseDto> addTasksToPersonalPlan(UUID userId, AddTasksToPersonalPlanRequestDto request);
    Mono<ActionResponseDto> addTasksToTeamPlan(UUID userId, AddTasksToTeamPlanRequestDto request);
    Mono<ActionResponseDto> updateTasksStatus(UUID userId, UpdateTasksStatusRequestDto request);
    Mono<ActionResponseDto> updateTasksAssignee(UUID userId, UpdateTasksAssigneeRequestDto request);
    Mono<ActionResponseDto> deleteTasks(UUID userId, DeleteTasksRequestDto request);
}
