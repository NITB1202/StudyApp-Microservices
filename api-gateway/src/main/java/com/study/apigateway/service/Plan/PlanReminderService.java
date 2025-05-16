package com.study.apigateway.service.Plan;

import com.study.apigateway.dto.Action.ActionResponseDto;
import com.study.apigateway.dto.Plan.Reminder.request.AddPlanRemindersRequestDto;
import com.study.apigateway.dto.Plan.Reminder.request.DeletePlanRemindersRequestDto;
import com.study.apigateway.dto.Plan.Reminder.request.UpdatePlanRemindersRequestDto;
import reactor.core.publisher.Mono;

public interface PlanReminderService {
    Mono<ActionResponseDto> addPlanReminders(AddPlanRemindersRequestDto request);
    Mono<ActionResponseDto> updatePlanReminders(UpdatePlanRemindersRequestDto request);
    Mono<ActionResponseDto> deletePlanReminders(DeletePlanRemindersRequestDto request);
}
