package com.nitb.planservice.repository;

import com.nitb.planservice.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PlanRepository extends JpaRepository<Plan, UUID> {
    List<Plan> findByTeamId(UUID teamId);
    List<Plan> findByCreatorIdAndTeamIdIsNull(UUID creatorId);
}