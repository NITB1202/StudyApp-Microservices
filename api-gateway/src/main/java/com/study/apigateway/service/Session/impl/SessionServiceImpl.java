package com.study.apigateway.service.Session.impl;

import com.study.apigateway.dto.Session.request.SaveSessionRequestDto;
import com.study.apigateway.dto.Session.response.SessionResponseDto;
import com.study.apigateway.dto.Session.response.SessionStatisticsResponseDto;
import com.study.apigateway.grpc.SessionServiceGrpcClient;
import com.study.apigateway.mapper.SessionMapper;
import com.study.apigateway.service.Session.SessionService;
import com.study.sessionservice.grpc.SessionResponse;
import com.study.sessionservice.grpc.SessionStatisticsResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.UUID;

@Service
@AllArgsConstructor
public class SessionServiceImpl implements SessionService {
    private final SessionServiceGrpcClient grpcClient;

    @Override
    public Mono<SessionResponseDto> saveSession(UUID userId, SaveSessionRequestDto request) {
        return Mono.fromCallable(()->{
            SessionResponse response = grpcClient.saveSession(userId, request);
            return SessionMapper.toSessionResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<SessionStatisticsResponseDto> getSessionWeeklyStatistics(UUID userId) {
        return Mono.fromCallable(()->{
            SessionStatisticsResponse response = grpcClient.getSessionWeeklyStatistics(userId);
            return SessionMapper.toSessionStatisticsResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
