package com.study.apigateway.mapper;

import com.study.apigateway.dto.Session.response.SessionResponseDto;
import com.study.apigateway.dto.Session.response.SessionStatisticsResponseDto;
import com.study.sessionservice.grpc.SessionResponse;
import com.study.sessionservice.grpc.SessionStatisticsResponse;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.UUID;

public class SessionMapper {
    private SessionMapper() {}

    public static SessionResponseDto toSessionResponseDto(SessionResponse session) {
        return SessionResponseDto.builder()
                .id(UUID.fromString(session.getId()))
                .studyDate(LocalDateTime.parse(session.getStudyDate()))
                .durationInMinutes(session.getDurationInMinutes())
                .elapsedTimeInMinutes(session.getElapsedTimeInMinutes())
                .build();
    }

    public static SessionStatisticsResponseDto toSessionStatisticsResponseDto(SessionStatisticsResponse statistics) {
        float roundedHours = BigDecimal.valueOf(statistics.getTotalHoursSpent())
                .setScale(2, RoundingMode.HALF_UP)
                .floatValue();

        return SessionStatisticsResponseDto.builder()
                .totalHoursSpent(roundedHours)
                .incompleteSessionsCount(statistics.getIncompleteSessionCount())
                .completedSessionsCount(statistics.getCompletedSessionCount())
                .build();
    }
}
