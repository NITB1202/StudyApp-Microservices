package com.study.documentservice.service;

import com.study.documentservice.entity.TeamUsage;
import com.study.documentservice.entity.UserUsage;
import com.study.documentservice.grpc.GetTeamUsageRequest;
import com.study.documentservice.grpc.GetUserUsageRequest;

public interface UsageService {
    UserUsage getUserUsage(GetUserUsageRequest request);
    TeamUsage getTeamUsage(GetTeamUsageRequest request);
}
