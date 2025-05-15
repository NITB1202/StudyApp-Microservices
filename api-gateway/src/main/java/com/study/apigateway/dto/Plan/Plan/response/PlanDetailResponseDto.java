package com.study.apigateway.dto.Plan.Plan.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.study.apigateway.dto.Plan.Reminder.response.PlanReminderResponseDto;
import com.study.apigateway.dto.Plan.Task.response.TaskResponseDto;
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
public class PlanDetailResponseDto {
    private UUID id;

    private Boolean isTeamPlan;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime completeAt;

    private String name;

    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endAt;

    private List<PlanReminderResponseDto> reminders;

    private List<TaskResponseDto> tasks;
}
