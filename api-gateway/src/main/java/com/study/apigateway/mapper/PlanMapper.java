package com.study.apigateway.mapper;

import com.study.apigateway.dto.Plan.Plan.response.PlanResponseDto;
import com.study.planservice.grpc.PlanResponse;

import java.util.UUID;

public class PlanMapper {
    private PlanMapper() {}

    public static PlanResponseDto toPlanResponseDto(PlanResponse plan) {
        return PlanResponseDto.builder()
                .id(UUID.fromString(plan.getId()))
                .build();
    }
}