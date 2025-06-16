package com.study.documentservice.service.impl;

import com.study.common.exceptions.BusinessException;
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
    private final UserUsageRepository userUsageRepository;
    private final TeamUsageRepository teamUsageRepository;
    private final static long PERSONAL_USAGE_LIMIT = 2147483648L; //2GB
    private final static long TEAM_USAGE_LIMIT = 2147483648L * 2; //4GB

    @Override
    public void createUserUsage(UUID userId) {
        UserUsage usage = UserUsage.builder()
                .userId(userId)
                .total(PERSONAL_USAGE_LIMIT)
                .used(0L)
                .build();

        userUsageRepository.save(usage);
    }

    @Override
    public void createTeamUsage(UUID teamId) {
        TeamUsage usage = TeamUsage.builder()
                .teamId(teamId)
                .total(TEAM_USAGE_LIMIT)
                .used(0L)
                .build();

        teamUsageRepository.save(usage);
    }

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

    @Override
    public void increaseUserUsage(UUID userId, long bytes) {
        UserUsage usage = userUsageRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User usage not found.")
        );

        if(usage.getTotal() - usage.getUsed() < bytes) {
            throw new BusinessException("User usage limit exceeded(2GB).");
        }

        usage.setUsed(usage.getUsed() + bytes);
        userUsageRepository.save(usage);
    }

    @Override
    public void increaseTeamUsage(UUID teamId, long bytes) {
        TeamUsage usage = teamUsageRepository.findById(teamId).orElseThrow(
                () -> new NotFoundException("Team usage not found.")
        );

        if(usage.getTotal() - usage.getUsed() < bytes) {
            throw new BusinessException("Team usage limit exceeded(4GB).");
        }

        usage.setUsed(usage.getUsed() + bytes);
        teamUsageRepository.save(usage);
    }

    @Override
    public void decreaseUserUsage(UUID userId, long bytes) {
        UserUsage usage = userUsageRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User usage not found.")
        );

        if(usage.getUsed() < bytes) {
            throw new BusinessException("Invalid file size.");
        }

        usage.setUsed(usage.getUsed() - bytes);
        userUsageRepository.save(usage);
    }

    @Override
    public void decreaseTeamUsage(UUID teamId, long bytes) {
        TeamUsage usage = teamUsageRepository.findById(teamId).orElseThrow(
                () -> new NotFoundException("Team usage not found.")
        );

        if(usage.getUsed() < bytes) {
            throw new BusinessException("Invalid file size.");
        }

        usage.setUsed(usage.getUsed() - bytes);
        teamUsageRepository.save(usage);
    }
}
