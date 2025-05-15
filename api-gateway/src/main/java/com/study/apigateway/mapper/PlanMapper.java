package com.study.apigateway.mapper;

import com.study.apigateway.dto.Plan.Plan.response.PlanDetailResponseDto;
import com.study.apigateway.dto.Plan.Plan.response.PlanResponseDto;
import com.study.apigateway.dto.Plan.Reminder.response.PlanReminderResponseDto;
import com.study.apigateway.dto.Plan.Task.response.TaskResponseDto;
import com.study.planservice.grpc.PlanDetailResponse;
import com.study.planservice.grpc.PlanRemindersResponse;
import com.study.planservice.grpc.PlanResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class PlanMapper {
    private PlanMapper() {}

    public static PlanResponseDto toPlanResponseDto(PlanResponse plan) {
        return PlanResponseDto.builder()
                .id(UUID.fromString(plan.getId()))
                .build();
    }

    public static PlanDetailResponseDto toPlanDetailResponseDto(PlanDetailResponse plan, PlanRemindersResponse reminders, List<TaskResponseDto> tasks) {
        List<PlanReminderResponseDto> remindersDto = new java.util.ArrayList<>
                (reminders.getReminderList().stream()
                .map(PlanReminderMapper::toPlanReminderResponseDto)
                .toList());

        LocalDateTime endAt = LocalDateTime.parse(plan.getEndAt());
        remindersDto.removeIf(reminder -> reminder.getRemindAt().equals(endAt));

        LocalDateTime completeAt = plan.getCompleteAt().isEmpty() ? null : LocalDateTime.parse(plan.getCompleteAt());

        return PlanDetailResponseDto.builder()
                .id(UUID.fromString(plan.getId()))
                .isTeamPlan(!plan.getTeamId().isEmpty())
                .completeAt(completeAt)
                .name(plan.getName())
                .description(plan.getDescription())
                .startAt(LocalDateTime.parse(plan.getStartAt()))
                .endAt(LocalDateTime.parse(plan.getEndAt()))
                .reminders(remindersDto)
                .tasks(tasks)
                .build();
    }
}