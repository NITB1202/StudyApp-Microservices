package com.study.apigateway.dto.Plan.Reminder.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePlanReminderRequestDto {
    @NotNull(message = "Id is required.")
    private UUID id;

    @NotNull(message = "Remind at is required.")
    private LocalDateTime remindAt;
}
