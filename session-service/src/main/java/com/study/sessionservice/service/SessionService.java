package com.study.sessionservice.service;

import com.study.sessionservice.entity.Session;
import com.study.sessionservice.grpc.SaveSessionRequest;

import java.time.LocalDateTime;
import java.util.UUID;

public interface SessionService {
    Session saveSession(SaveSessionRequest request);
    float getTotalHoursSpent(UUID userId, LocalDateTime from, LocalDateTime to);
    long getIncompleteSessionCount(UUID userId, LocalDateTime from, LocalDateTime to);
    long getCompletedSessionCount(UUID userId, LocalDateTime from, LocalDateTime to);
}
