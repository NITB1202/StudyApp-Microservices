package com.study.teamservice.repository;

import com.study.teamservice.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TeamRepository extends JpaRepository<Team, UUID> {
    boolean existsByNameAndCreatorId(String name, UUID creatorId);
    Team findByTeamCode(String teamCode);
}