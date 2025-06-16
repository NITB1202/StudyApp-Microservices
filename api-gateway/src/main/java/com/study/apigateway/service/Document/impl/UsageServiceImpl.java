package com.study.apigateway.service.Document.impl;

import com.study.apigateway.dto.Document.Usage.UsageResponseDto;
import com.study.apigateway.grpc.DocumentServiceGrpcClient;
import com.study.apigateway.mapper.UsageMapper;
import com.study.apigateway.service.Document.UsageService;
import com.study.documentservice.grpc.UsageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsageServiceImpl implements UsageService {
    private final DocumentServiceGrpcClient documentClient;

    @Override
    public Mono<UsageResponseDto> getUserUsage(UUID userId) {
        return Mono.fromCallable(()->{
            UsageResponse response = documentClient.getUserUsage(userId);
            return UsageMapper.toUsageResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<UsageResponseDto> getTeamUsage(UUID teamId) {
        return Mono.fromCallable(()->{
            UsageResponse response = documentClient.getTeamUsage(teamId);
            return UsageMapper.toUsageResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
