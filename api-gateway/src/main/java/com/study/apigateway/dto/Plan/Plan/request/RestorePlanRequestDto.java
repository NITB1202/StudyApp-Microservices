package com.study.apigateway.dto.Plan.Plan.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestorePlanRequestDto {
    @NotNull(message = "End at is required.")
    private LocalDateTime endAt;
}
