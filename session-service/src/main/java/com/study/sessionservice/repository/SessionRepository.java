package com.study.sessionservice.repository;

import com.study.sessionservice.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.UUID;

public interface SessionRepository extends JpaRepository<Session, UUID> {
    @Query("SELECT COALESCE(SUM(s.elapsedTimeInMinutes), 0) FROM Session s WHERE s.userId = :userId AND s.studyDate BETWEEN :from AND :to")
    Integer getStudyMinutesInWeek(@Param("userId") UUID userId,
                                  @Param("from") LocalDateTime from,
                                  @Param("to") LocalDateTime to);
    @Query("SELECT COUNT(s) FROM Session s WHERE s.userId = :userId AND s.studyDate BETWEEN :from AND :to AND s.elapsedTimeInMinutes < s.durationInMinutes")
    Long countIncompleteSession(UUID userId, LocalDateTime from, LocalDateTime to);
    @Query("SELECT COUNT(s) FROM Session s WHERE s.userId = :userId AND s.studyDate BETWEEN :from AND :to AND s.elapsedTimeInMinutes = s.durationInMinutes")
    Long countCompletedSession(UUID userId, LocalDateTime from, LocalDateTime to);
}