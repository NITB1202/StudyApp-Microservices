package com.study.apigateway.dto.Plan.Task.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTaskRequestDto {
    @NotEmpty(message = "Task name is required.")
    private String name;

    @NotNull(message = "Assignee id is required.")
    private UUID assigneeId;
}
