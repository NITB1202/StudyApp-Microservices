package com.study.apigateway.mapper;

import com.study.apigateway.dto.Plan.Task.request.CreateTaskRequestDto;
import com.study.apigateway.dto.Plan.Task.request.UpdateTaskAssigneeRequestDto;
import com.study.apigateway.dto.Plan.Task.request.UpdateTaskStatusRequestDto;
import com.study.apigateway.dto.Plan.Task.response.TaskResponseDto;
import com.study.planservice.grpc.CreateTaskRequest;
import com.study.planservice.grpc.TaskResponse;
import com.study.planservice.grpc.UpdateTaskAssigneeRequest;
import com.study.planservice.grpc.UpdateTaskStatusRequest;
import com.study.userservice.grpc.UserDetailResponse;

import java.util.UUID;

public class TaskMapper {
    private TaskMapper() {}

    public static CreateTaskRequest toCreateTaskRequest(CreateTaskRequestDto dto) {
        return CreateTaskRequest.newBuilder()
                .setName(dto.getName())
                .setAssigneeId(dto.getAssigneeId().toString())
                .build();
    }

    public static CreateTaskRequestDto toCreateTaskRequestDto(String name, UUID assigneeId) {
        return CreateTaskRequestDto.builder()
                .name(name)
                .assigneeId(assigneeId)
                .build();
    }

    public static TaskResponseDto toTaskResponseDto(TaskResponse task, UserDetailResponse assignee) {
        return TaskResponseDto.builder()
                .id(UUID.fromString(task.getId()))
                .name(task.getName())
                .assigneeId(UUID.fromString(task.getAssigneeId()))
                .assigneeAvatarUrl(assignee.getAvatarUrl())
                .build();
    }

    public static UpdateTaskStatusRequest toUpdateTaskStatusRequest(UpdateTaskStatusRequestDto dto) {
        return UpdateTaskStatusRequest.newBuilder()
                .setTaskId(dto.getId().toString())
                .setIsCompleted(dto.getIsCompleted())
                .build();
    }

    public static UpdateTaskAssigneeRequest toUpdateTaskAssigneeRequest(UpdateTaskAssigneeRequestDto dto) {
        return UpdateTaskAssigneeRequest.newBuilder()
                .setTaskId(dto.getId().toString())
                .setAssigneeId(dto.getAssigneeId().toString())
                .build();
    }
}
