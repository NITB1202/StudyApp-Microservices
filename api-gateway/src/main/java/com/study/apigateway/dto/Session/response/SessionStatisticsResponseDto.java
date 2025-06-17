package com.study.apigateway.dto.Session.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessionStatisticsResponseDto {
    private float totalHoursSpent;

    private long incompleteSessionsCount;

    private long completedSessionsCount;
}
