package com.study.apigateway.dto.Plan.Reminder.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePlanRemindersRequestDto {
    @NotNull(message = "Reminders are required.")
    @Size(min = 1, message = "Must have at least 1 reminder.")
    private List<UpdatePlanReminderRequestDto> reminders;
}
