package com.study.teamservice.repository;

import com.study.common.enums.TeamRole;
import com.study.teamservice.entity.TeamUser;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface TeamUserRepository extends JpaRepository<TeamUser, UUID> {
    List<TeamUser> findByUserIdAndJoinDateBeforeAndIdGreaterThanOrderByJoinDateDescIdAsc(UUID userId, LocalDate cursorJoinDate, UUID cursorId, Pageable pageable);
    List<TeamUser> findByUserIdOrderByJoinDateDescIdAsc(UUID userId, Pageable pageable);
    Long countByUserId(UUID userId);
    TeamUser findByUserIdAndTeamId(UUID userId, UUID teamId);
    List<TeamUser> findByTeamId(UUID teamId);
    boolean existsByUserIdAndTeamId(UUID userId, UUID teamId);
    List<TeamUser> findByTeamIdAndRoleInAndJoinDateGreaterThanAndIdGreaterThan(UUID teamId, List<TeamRole> roles, LocalDate joinDate, UUID cursorId, Pageable pageable);
    List<TeamUser> findByTeamIdAndRoleIn(UUID teamId, List<TeamRole> roleOrder, Pageable pageable);
    Long countByTeamId(UUID teamId);
    @Query("SELECT tu FROM TeamUser tu " +
            "JOIN Team t ON tu.teamId = t.id " +
            "WHERE tu.userId = :userId " +
            "AND LOWER(t.name) LIKE LOWER(:keyword) " +
            "AND (tu.joinDate < :cursorJoinDate OR (tu.joinDate = :cursorJoinDate AND tu.id > :cursorId)) " +
            "ORDER BY tu.joinDate DESC, tu.id ASC")
    List<TeamUser> searchTeamsByUserAndNameWithCursor(
            @Param("userId") UUID userId,
            @Param("keyword") String keyword,
            @Param("cursorJoinDate") LocalDate cursorJoinDate,
            @Param("cursorId") UUID cursorId,
            Pageable pageable);
    @Query("SELECT tu FROM TeamUser tu " +
            "JOIN Team t ON tu.teamId = t.id " +
            "WHERE tu.userId = :userId " +
            "AND LOWER(t.name) LIKE LOWER(:keyword) " +
            "ORDER BY tu.joinDate DESC, tu.id ASC")
    List<TeamUser> searchTeamsByUserAndName(
            @Param("userId") UUID userId,
            @Param("keyword") String keyword,
            Pageable pageable);
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