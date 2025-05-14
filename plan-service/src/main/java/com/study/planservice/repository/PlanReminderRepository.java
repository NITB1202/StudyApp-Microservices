package com.study.planservice.repository;

import com.study.planservice.entity.PlanReminder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface PlanReminderRepository extends JpaRepository<PlanReminder, UUID> {
    List<PlanReminder> findAllByPlanId(UUID planId);
    void deleteAllByPlanId(UUID planId);
    boolean existsByPlanIdAndRemindAt(UUID planId, LocalDateTime remindAt);
    PlanReminder findByPlanIdAndRemindAt(UUID planId, LocalDateTime remindAt);
}