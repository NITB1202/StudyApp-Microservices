package com.study.apigateway.dto.Plan.Plan.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePersonalPlanRequestDto {
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
    private List<String> tasks;
}
