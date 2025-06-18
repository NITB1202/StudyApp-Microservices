package com.study.apigateway.service.Plan;

import com.study.apigateway.dto.Action.ActionResponseDto;
import com.study.apigateway.dto.Plan.Reminder.request.AddPlanRemindersRequestDto;
import com.study.apigateway.dto.Plan.Reminder.request.DeletePlanRemindersRequestDto;
import com.study.apigateway.dto.Plan.Reminder.request.UpdatePlanRemindersRequestDto;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface PlanReminderService {
    Mono<ActionResponseDto> addPlanReminders(UUID userId, UUID planId, AddPlanRemindersRequestDto request);
    Mono<ActionResponseDto> updatePlanReminders(UUID userId, UUID planId, UpdatePlanRemindersRequestDto request);
    Mono<ActionResponseDto> deletePlanReminders(UUID userId, UUID planId, DeletePlanRemindersRequestDto request);
}
