package com.study.apigateway.dto.Plan.Plan.request;

import com.study.apigateway.dto.Plan.Task.request.CreateTaskRequestDto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTeamPlanRequestDto {
    @NotNull(message = "Team id is required.")
    private UUID teamId;

    @NotEmpty(message = "Name is required.")
    private String name;

    private String description;

    @NotNull(message = "Start at is required.")
    private LocalDateTime startAt;

    @NotNull(message = "End at is required.")
    private LocalDateTime endAt;

    private List<LocalDateTime> remindTimes;

    @NotNull(message = "Tasks are required.")
    @Size(min = 1, message = "Each plan must have at least 1 task.")
    private List<CreateTaskRequestDto> tasks;
}
