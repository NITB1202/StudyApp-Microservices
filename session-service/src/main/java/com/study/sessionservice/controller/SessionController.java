package com.study.sessionservice.controller;

import com.study.sessionservice.grpc.*;
import com.study.sessionservice.service.SessionService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class SessionController extends SessionServiceGrpc.SessionServiceImplBase {
    private final SessionService sessionService;

    @Override
    public void saveSession(SaveSessionRequest request, StreamObserver<SessionResponse> responseObserver) {

    }

    @Override
    public void getSessionWeeklyStatistics(GetSessionWeeklyStatisticsRequest request, StreamObserver<SessionStatisticsResponse> responseObserver) {

    }
}
