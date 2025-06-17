package com.study.apigateway.grpc;

import com.study.apigateway.dto.Session.request.SaveSessionRequestDto;
import com.study.sessionservice.grpc.*;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SessionServiceGrpcClient {
    @GrpcClient("session-service")
    private SessionServiceGrpc.SessionServiceBlockingStub blockingStub;

    public SessionResponse saveSession(UUID userId, SaveSessionRequestDto dto) {
        SaveSessionRequest request = SaveSessionRequest.newBuilder()
                .setUserId(userId.toString())
                .setDurationInMinutes(dto.getDurationInMinutes())
                .setElapsedTimeInMinutes(dto.getElapsedTimeInMinutes())
                .build();

        return blockingStub.saveSession(request);
    }

    public SessionStatisticsResponse getSessionWeeklyStatistics(UUID userId) {
        GetSessionWeeklyStatisticsRequest request = GetSessionWeeklyStatisticsRequest.newBuilder()
                .setUserId(userId.toString())
                .build();

        return blockingStub.getSessionWeeklyStatistics(request);
    }
}
