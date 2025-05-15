package com.study.apigateway.dto.Plan.Plan.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanStatisticsResponseDto {
    private long finishedPlans;

    private float finishedPlansPercent;
}
