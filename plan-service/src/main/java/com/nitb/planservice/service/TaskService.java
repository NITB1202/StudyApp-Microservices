package com.nitb.planservice.service;

import com.nitb.planservice.entity.Plan;

import java.util.List;
import java.util.UUID;

public interface TaskService {
    List<Plan> getAssignedPlans(UUID userId);
    boolean isAssignedForPlan(UUID userId, UUID planId);
}
