package com.study.apigateway.dto.Plan.Reminder.request;

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
public class AddPlanRemindersRequestDto {
    @NotNull(message = "Plan id is required.")
    private UUID planId;

    @NotNull(message = "Reminders are required.")
    @Size(min = 1, message = "Must have at least 1 reminder.")
    private List<LocalDateTime> reminders;
}
