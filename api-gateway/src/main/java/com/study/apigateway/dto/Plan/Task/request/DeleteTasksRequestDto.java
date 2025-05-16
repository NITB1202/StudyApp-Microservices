package com.study.apigateway.dto.Plan.Task.request;

import jakarta.validation.constraints.NotNull;
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
public class DeleteTasksRequestDto {
    @NotNull(message = "Plan id is required.")
    private UUID planId;

    @NotNull(message = "Tasks id are required.")
    private List<UUID> taskIds;
}
