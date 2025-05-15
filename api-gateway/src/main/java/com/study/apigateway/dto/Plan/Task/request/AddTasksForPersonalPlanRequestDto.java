package com.study.apigateway.dto.Plan.Task.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddTasksForPersonalPlanRequestDto {
    @NotNull(message = "Plan id is required.")
    private UUID planId;

    @NotNull(message = "Tasks are required.")
    @Size(min = 1, message = "Must have at least 1 task.")
    private List<String> tasks;
}
