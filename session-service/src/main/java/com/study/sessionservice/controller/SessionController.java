package com.study.sessionservice.controller;

import com.study.sessionservice.entity.Session;
import com.study.sessionservice.grpc.*;
import com.study.sessionservice.mapper.SessionMapper;
import com.study.sessionservice.service.SessionService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@GrpcService
@RequiredArgsConstructor
public class SessionController extends SessionServiceGrpc.SessionServiceImplBase {
    private final SessionService sessionService;

    @Override
    public void saveSession(SaveSessionRequest request, StreamObserver<SessionResponse> responseObserver) {
        Session session = sessionService.saveSession(request);
        SessionResponse response = SessionMapper.toSessionResponse(session);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getSessionWeeklyStatistics(GetSessionWeeklyStatisticsRequest request, StreamObserver<SessionStatisticsResponse> responseObserver) {
        UUID userId = UUID.fromString(request.getUserId());
        LocalDateTime startOfWeek = LocalDate.now().with(DayOfWeek.MONDAY).atStartOfDay();
        LocalDateTime endOfWeek = LocalDate.now().with(DayOfWeek.SUNDAY).atTime(LocalTime.MAX);

        float totalHoursSpent = sessionService.getTotalHoursSpent(userId, startOfWeek, endOfWeek);
        long incompleteSessionCount = sessionService.getIncompleteSessionCount(userId, startOfWeek, endOfWeek);
        long completedSessionCount = sessionService.getCompletedSessionCount(userId, startOfWeek, endOfWeek);

        SessionStatisticsResponse response = SessionMapper.toSessionStatisticsResponse(totalHoursSpent, incompleteSessionCount, completedSessionCount);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
