package com.study.apigateway.dto.Session.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveSessionRequestDto {
    @NotNull(message = "Duration is required.")
    private Integer durationInMinutes;

    @NotNull(message = "Elapsed time is required.")
    private Integer elapsedTimeInMinutes;
}
