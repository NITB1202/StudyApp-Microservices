package com.study.apigateway.service.Document;

import com.study.apigateway.dto.Document.Usage.UsageResponseDto;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UsageService {
    Mono<UsageResponseDto> getUserUsage(UUID userId);
    Mono<UsageResponseDto> getTeamUsage(UUID teamId);
}
