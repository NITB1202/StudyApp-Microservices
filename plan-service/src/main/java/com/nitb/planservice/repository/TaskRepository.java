package com.nitb.planservice.repository;

import com.nitb.planservice.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
    boolean existsByNameAndPlanId(String name, UUID planId);
    List<Task> findByPlanId(UUID planId);
    int countTaskByPlanId(UUID planId);
    int countTaskByPlanIdAndIsCompletedTrue(UUID planId);
    boolean existsByPlanIdAndAssigneeId(UUID planId, UUID assigneeId);
    @Query("SELECT DISTINCT t.planId FROM Task t WHERE t.assigneeId = :userId")
    List<UUID> findPlanIdsByAssigneeId(@Param("userId") UUID userId);
    void deleteAllByPlanId(UUID planId);
    @Query("SELECT DISTINCT t.assigneeId FROM Task t WHERE t.planId = :planId")
    List<UUID> findAllAssigneeIdsByPlanId(@Param("planId") UUID planId);
}