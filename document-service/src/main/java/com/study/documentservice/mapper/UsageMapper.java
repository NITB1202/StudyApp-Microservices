package com.study.documentservice.mapper;

import com.study.documentservice.entity.TeamUsage;
import com.study.documentservice.entity.UserUsage;
import com.study.documentservice.grpc.UsageResponse;

public class UsageMapper {
    private UsageMapper() {}

    public static UsageResponse toUsageResponse(UserUsage usage) {
        return UsageResponse.newBuilder()
                .setUsed(usage.getUsed())
                .setTotal(usage.getTotal())
                .build();
    }

    public static UsageResponse toUsageResponse(TeamUsage usage) {
        return UsageResponse.newBuilder()
                .setUsed(usage.getUsed())
                .setTotal(usage.getTotal())
                .build();
    }
}
