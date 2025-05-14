package com.study.planservice.repository;

import com.study.planservice.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface PlanRepository extends JpaRepository<Plan, UUID> {
    List<Plan> findByTeamId(UUID teamId);
    @Query("""
        SELECT DISTINCT p
        FROM Plan p
        JOIN Task t ON t.planId = p.id
        WHERE t.assigneeId = :userId
        AND :date BETWEEN p.startAt AND p.endAt
    """)
    List<Plan> findAssignedPlansByUserIdAndDate(@Param("userId") UUID userId, @Param("date") LocalDateTime dateTime);
    @Query("""
        SELECT p
        FROM Plan p
        WHERE p.teamId = :teamId
        AND :date BETWEEN p.startAt AND p.endAt
    """)
    List<Plan> findTeamPlansByTeamIdAndDate(@Param("teamId") UUID teamId, @Param("date") LocalDateTime dateTime);
    @Query("""
        SELECT DISTINCT p
        FROM Plan p
        JOIN Task t ON t.planId = p.id
        WHERE t.assigneeId = :userId
        AND p.endAt BETWEEN :startTime AND :endTime
    """)
    List<Plan> findAssignedPlansByUserIdAndEndAtBetween(@Param("userId") UUID userId,
                                                        @Param("startTime") LocalDateTime startTime,
                                                        @Param("endTime") LocalDateTime endTime);
    List<Plan> findPlansByTeamIdAndEndAtBetween(UUID teamId, LocalDateTime start, LocalDateTime end);
    List<Plan> findPlansByCreatorIdAndTeamIdIsNullAndCompleteAtIsNullAndEndAtBefore(UUID creatorId, LocalDateTime time);
    List<Plan> findPlansByTeamIdAndCompleteAtIsNullAndEndAtBefore(UUID teamId, LocalDateTime time);
    List<Plan> findAllByCompleteAtNullAndEndAtBefore(LocalDateTime time);
    List<Plan> findByTeamIdAndEndAtBefore(UUID teamId, LocalDateTime time);
    @Query("""
        SELECT DISTINCT p
        FROM Plan p
        JOIN Task t ON t.planId = p.id
        WHERE t.assigneeId = :userId
          AND p.teamId = :teamId
          AND p.endAt >= :now
    """)
    List<Plan> findAssignedTeamPlansFromNowOn(
            @Param("userId") UUID userId,
            @Param("teamId") UUID teamId,
            @Param("now") LocalDateTime now
    );


}