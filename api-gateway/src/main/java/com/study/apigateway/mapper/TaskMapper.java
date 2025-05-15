package com.study.apigateway.mapper;

import com.study.apigateway.dto.Plan.Task.request.CreateTaskRequestDto;
import com.study.planservice.grpc.CreateTaskRequest;

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
}
