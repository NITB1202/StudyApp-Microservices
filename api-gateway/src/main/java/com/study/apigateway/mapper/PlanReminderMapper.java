package com.study.apigateway.mapper;

import com.study.apigateway.dto.Plan.Reminder.response.PlanReminderResponseDto;
import com.study.planservice.grpc.PlanReminderResponse;

import java.time.LocalDateTime;
import java.util.UUID;

public class PlanReminderMapper {
    private PlanReminderMapper() {}

    public static PlanReminderResponseDto toPlanReminderResponseDto(PlanReminderResponse reminder) {
        return PlanReminderResponseDto.builder()
                .id(UUID.fromString(reminder.getId()))
                .remindAt(LocalDateTime.parse(reminder.getRemindAt()))
                .build();
    }
}
