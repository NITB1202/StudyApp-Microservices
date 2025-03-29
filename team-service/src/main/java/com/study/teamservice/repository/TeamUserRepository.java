package com.study.teamservice.repository;

import com.study.teamservice.entity.TeamUser;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface TeamUserRepository extends JpaRepository<TeamUser, UUID> {
    TeamUser getFirstByUserIdOrderByJoinDateDesc(UUID userId);
    List<TeamUser> findByUserIdOrderByJoinDateDesc(UUID userId, Pageable pageable);
    List<TeamUser> findByUserIdAndJoinDateBeforeOrderByJoinDateDesc(UUID userId, LocalDate joinDate, Pageable pageable);
    Long countByUserId(UUID userId);
    TeamUser findByUserIdAndTeamId(UUID userId, UUID teamId);
    List<TeamUser> findByTeamId(UUID teamId);
}