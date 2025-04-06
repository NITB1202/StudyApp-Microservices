package com.study.apigateway.dto.Notification.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateInvitationRequestDto {
    @NotNull(message = "Team id is required")
    private UUID teamId;

    @NotNull(message = "Invitee id is required")
    private UUID inviteeId;
}
