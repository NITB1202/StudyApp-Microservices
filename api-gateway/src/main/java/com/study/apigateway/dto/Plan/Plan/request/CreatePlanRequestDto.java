package com.study.apigateway.dto.Plan.Plan.request;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
@Builder
public class CreatePlanRequestDto {
    private String name;

    private String description;

    private LocalDateTime startAt;

    private LocalDateTime endAt;
}
