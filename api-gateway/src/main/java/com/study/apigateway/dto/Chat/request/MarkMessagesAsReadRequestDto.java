package com.study.apigateway.dto.Chat.request;

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
public class MarkMessagesAsReadRequestDto {
    @NotNull(message = "Ids are required.")
    @Size(min = 1, message = "The list must contain at least 1 item.")
    private List<UUID> messageIds;
}
