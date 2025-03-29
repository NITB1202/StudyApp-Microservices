package com.study.teamservice.repository;

import com.study.teamservice.entity.Team;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface TeamRepository extends JpaRepository<Team, UUID> {
    @Query("""
    SELECT t FROM Team t
    JOIN TeamUser tu ON tu.teamId = t.id
    WHERE tu.userId = :userId
      AND LOWER(t.name) LIKE LOWER(:keyword)
      AND tu.joinDate < :cursor
    ORDER BY tu.joinDate DESC
    """)
    List<Team> searchTeamsByUserAndNameWithCursor(
            @Param("userId") UUID userId,
            @Param("keyword") String keyword,
            @Param("cursor") LocalDate cursor,
            Pageable pageable
    );

    @Query("""
    SELECT t FROM Team t
    JOIN TeamUser tu ON tu.teamId = t.id
    WHERE tu.userId = :userId
      AND LOWER(t.name) LIKE LOWER(:keyword)
    ORDER BY tu.joinDate DESC
    """)
    List<Team> searchTeamsByUserAndName(
            @Param("userId") UUID userId,
            @Param("keyword") String keyword,
            Pageable pageable
    );

    @Query("""
    SELECT COUNT(t)
    FROM Team t
    JOIN TeamUser tu ON t.id = tu.teamId
    WHERE tu.userId = :userId
    AND LOWER(t.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
""")
    long countUserTeamsByKeyword(@Param("userId") UUID userId,
                                 @Param("keyword") String keyword);

}