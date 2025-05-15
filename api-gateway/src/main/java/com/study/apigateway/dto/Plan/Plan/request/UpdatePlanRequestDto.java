package com.study.apigateway.dto.Plan.Plan.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePlanRequestDto {
    private String name;

    private String description;

    private LocalDateTime startAt;

    private LocalDateTime endAt;
}
