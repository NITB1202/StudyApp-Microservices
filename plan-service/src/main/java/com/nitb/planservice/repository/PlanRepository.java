package com.nitb.planservice.repository;

import com.nitb.planservice.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface PlanRepository extends JpaRepository<Plan, UUID> {
    List<Plan> findByCreatorId(UUID creatorId);
    List<Plan> findByTeamIdAndCreatorId(UUID teamId, UUID creatorId);
    List<Plan> findByTeamId(UUID teamId);

    @Query("SELECT p FROM Plan p " +
            "WHERE p.creatorId = :userId " +
            "AND :datetime BETWEEN p.startAt AND p.endAt")
    List<Plan> findPlansByUserIdAndDatetimeWithinRange(@Param("userId") UUID userId,
                                                       @Param("datetime") LocalDateTime datetime);

    @Query("SELECT p FROM Plan p " +
            "WHERE p.creatorId = :userId " +
            "AND p.teamId = :teamId "+
            "AND :datetime BETWEEN p.startAt AND p.endAt")
    List<Plan> findPlansByUserIdAndTeamIdAndDatetimeWithinRange(@Param("userId") UUID userId,
                                                       @Param("teamId") UUID teamId,
                                                       @Param("datetime") LocalDateTime datetime);


}