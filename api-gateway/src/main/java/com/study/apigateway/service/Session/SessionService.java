package com.study.apigateway.service.Session;

import com.study.apigateway.dto.Session.request.SaveSessionRequestDto;
import com.study.apigateway.dto.Session.response.SessionResponseDto;
import com.study.apigateway.dto.Session.response.SessionStatisticsResponseDto;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface SessionService {
    Mono<SessionResponseDto> saveSession(UUID userId, SaveSessionRequestDto request);
    Mono<SessionStatisticsResponseDto> getSessionWeeklyStatistics(UUID userId);
}
