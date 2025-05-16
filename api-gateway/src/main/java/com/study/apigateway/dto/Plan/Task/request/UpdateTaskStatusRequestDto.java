package com.study.apigateway.dto.Plan.Task.request;

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
public class UpdateTaskStatusRequestDto {
    @NotNull(message = "Id is required.")
    private UUID id;

    @NotNull(message = "IsCompleted is required.")
    private Boolean isCompleted;
}
