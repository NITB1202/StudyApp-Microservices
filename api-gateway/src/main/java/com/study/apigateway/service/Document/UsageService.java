package com.study.apigateway.service.Document;

import com.study.apigateway.dto.Document.Usage.UsageResponseDto;

import java.util.UUID;

public interface UsageService {
    UsageResponseDto getUserUsage(UUID userId);
    UsageResponseDto getTeamUsage(UUID teamId);
}
