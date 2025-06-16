package com.study.apigateway.mapper;

import com.study.apigateway.dto.Document.Usage.UsageResponseDto;
import com.study.documentservice.grpc.UsageResponse;

public class UsageMapper {
    private UsageMapper() {}

    public static UsageResponseDto toUsageResponseDto(UsageResponse usage) {
        return UsageResponseDto.builder()
                .used(usage.getUsed())
                .total(usage.getTotal())
                .build();
    }
}
