package com.study.apigateway.dto.Plan.Task.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponseDto {
    private UUID id;

    private String name;

    private UUID assigneeId;

    private String assigneeAvatarUrl;

    private boolean isCompleted;
}
