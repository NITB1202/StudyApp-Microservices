package com.study.documentservice.service.impl;

import com.study.common.exceptions.NotFoundException;
import com.study.documentservice.entity.TeamUsage;
import com.study.documentservice.entity.UserUsage;
import com.study.documentservice.grpc.GetTeamUsageRequest;
import com.study.documentservice.grpc.GetUserUsageRequest;
import com.study.documentservice.repository.TeamUsageRepository;
import com.study.documentservice.repository.UserUsageRepository;
import com.study.documentservice.service.UsageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsageServiceImpl implements UsageService {
    private UserUsageRepository userUsageRepository;
    private TeamUsageRepository teamUsageRepository;

    @Override
    public UserUsage getUserUsage(GetUserUsageRequest request) {
        UUID userId = UUID.fromString(request.getUserId());
        return userUsageRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User usage not found.")
        );
    }

    @Override
    public TeamUsage getTeamUsage(GetTeamUsageRequest request) {
        UUID teamId = UUID.fromString(request.getTeamId());
        return teamUsageRepository.findById(teamId).orElseThrow(
                () -> new NotFoundException("Team usage not found.")
        );
    }
}
