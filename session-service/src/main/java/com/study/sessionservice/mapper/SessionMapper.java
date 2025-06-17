package com.study.sessionservice.mapper;

import com.study.sessionservice.entity.Session;
import com.study.sessionservice.grpc.SessionResponse;
import com.study.sessionservice.grpc.SessionStatisticsResponse;

public class SessionMapper {
    private SessionMapper() {}

    public static SessionResponse toSessionResponse(Session session) {
        return SessionResponse.newBuilder()
                .setId(session.getId().toString())
                .setStudyDate(session.getStudyDate().toString())
                .setDurationInMinutes(session.getDurationInMinutes())
                .setElapsedTimeInMinutes(session.getElapsedTimeInMinutes())
                .build();
    }

    public static SessionStatisticsResponse toSessionStatisticsResponse(float totalHoursSpent, long incompleteSessionCount, long completedSessionCount) {
        return SessionStatisticsResponse.newBuilder()
                .setTotalHoursSpent(totalHoursSpent)
                .setIncompleteSessionCount(incompleteSessionCount)
                .setCompletedSessionCount(completedSessionCount)
                .build();
    }
}
