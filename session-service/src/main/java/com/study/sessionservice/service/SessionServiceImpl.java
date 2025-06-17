package com.study.sessionservice.service;

import com.study.common.exceptions.BusinessException;
import com.study.sessionservice.entity.Session;
import com.study.sessionservice.grpc.SaveSessionRequest;
import com.study.sessionservice.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {
    private final SessionRepository sessionRepository;

    @Override
    public Session saveSession(SaveSessionRequest request) {
        if(request.getDurationInMinutes() < 0) {
            throw new BusinessException("Invalid duration.");
        }

        if(request.getElapsedTimeInMinutes() < 0) {
            throw new BusinessException("Invalid elapsed time.");
        }

        if(request.getDurationInMinutes() < request.getElapsedTimeInMinutes()) {
            throw new BusinessException("The duration must be greater than or equal to the elapsed time.");
        }

        Session session = Session.builder()
                .userId(UUID.fromString(request.getUserId()))
                .studyDate(LocalDateTime.now())
                .durationInMinutes(request.getDurationInMinutes())
                .elapsedTimeInMinutes(request.getElapsedTimeInMinutes())
                .build();

        return sessionRepository.save(session);
    }

    @Override
    public float getTotalHoursSpent(UUID userId, LocalDateTime from, LocalDateTime to) {
        int minutes = sessionRepository.getStudyMinutesInWeek(userId, from, to);
        return (float)minutes / 60;
    }

    @Override
    public long getIncompleteSessionCount(UUID userId, LocalDateTime from, LocalDateTime to) {
        return sessionRepository.countIncompleteSession(userId, from, to);
    }

    @Override
    public long getCompletedSessionCount(UUID userId, LocalDateTime from, LocalDateTime to) {
        return sessionRepository.countCompletedSession(userId, from, to);
    }
}
