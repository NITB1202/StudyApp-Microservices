package com.study.apigateway.dto.Plan.Reminder.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeletePlanRemindersRequestDto {
    @NotNull(message = "Reminders id are required.")
    @Size(min = 1, message = "Must have at least 1 reminder.")
    private List<UUID> reminderIds;
}
