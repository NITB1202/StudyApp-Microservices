package com.study.documentservice.service;

import com.study.documentservice.entity.TeamUsage;
import com.study.documentservice.entity.UserUsage;
import com.study.documentservice.grpc.GetTeamUsageRequest;
import com.study.documentservice.grpc.GetUserUsageRequest;

import java.util.UUID;

public interface UsageService {
    void createUserUsage(UUID userId);
    void createTeamUsage(UUID teamId);
    UserUsage getUserUsage(GetUserUsageRequest request);
    TeamUsage getTeamUsage(GetTeamUsageRequest request);
    void increaseUserUsage(UUID userId, long bytes);
    void increaseTeamUsage(UUID teamId, long bytes);
    void decreaseUserUsage(UUID userId, long bytes);
    void decreaseTeamUsage(UUID teamId, long bytes);
}
