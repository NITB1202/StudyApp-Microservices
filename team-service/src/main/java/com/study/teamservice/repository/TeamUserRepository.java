package com.study.teamservice.repository;

import com.study.common.enums.TeamRole;
import com.study.teamservice.entity.Team;
import com.study.teamservice.entity.TeamUser;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface TeamUserRepository extends JpaRepository<TeamUser, UUID> {
    List<TeamUser> findByUserIdOrderByJoinDateDesc(UUID userId, Pageable pageable);
    List<TeamUser> findByUserIdAndJoinDateBeforeOrderByJoinDateDesc(UUID userId, LocalDate joinDate, Pageable pageable);
    Long countByUserId(UUID userId);
    TeamUser findByUserIdAndTeamId(UUID userId, UUID teamId);
    List<TeamUser> findByTeamId(UUID teamId);
    boolean existsByUserIdAndTeamId(UUID userId, UUID teamId);
    List<TeamUser> findByTeamIdAndRoleInAndJoinDateGreaterThan(UUID teamId, List<TeamRole> roleOrder, LocalDate cursor, Pageable pageable);
    List<TeamUser> findByTeamIdAndRoleIn(UUID teamId, List<TeamRole> roleOrder, Pageable pageable);
    Long countByTeamId(UUID teamId);
    @Query("""
    SELECT COUNT(tu) 
    FROM TeamUser tu 
    WHERE tu.teamId = :teamId 
      AND tu.role <> 'MEMBER'
    """)
    long countNonMemberByTeamId(@Param("teamId") UUID teamId);
    @Query("""
    SELECT t FROM TeamUser tu
    JOIN Team t ON tu.teamId = t.id
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
    SELECT t FROM TeamUser tu
    JOIN Team t ON tu.teamId = t.id
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
    FROM TeamUser tu
    JOIN Team t ON tu.teamId = t.id
    WHERE tu.userId = :userId
      AND LOWER(t.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
""")
    long countUserTeamsByKeyword(@Param("userId") UUID userId, @Param("keyword") String keyword);
    void deleteByUserIdAndTeamId(UUID userId, UUID teamId);
    void deleteAllByTeamId(UUID teamId);
}