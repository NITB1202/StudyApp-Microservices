package com.nitb.planservice.service;

import com.nitb.planservice.entity.Plan;
import com.study.planservice.grpc.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface PlanService {
    Plan createPlan(CreatePlanRequest request);
    Plan getPlanById(GetPlanByIdRequest request);
    List<Plan> getAssignedPlansOnDate(GetAssignedPlansOnDateRequest request);
    List<Plan> getTeamPlansOnDate(GetTeamPlansOnDateRequest request);
    Set<LocalDate> getDatesWithDeadlineInMonth(GetDatesWithDeadlineInMonthRequest request);
    List<Plan> getPersonalMissedPlans(GetPersonalMissedPlansRequest request);
    List<Plan> getTeamMissedPlans(GetTeamMissedPlansRequest request);
    Plan updatePlan(UpdatePlanRequest request);
    void deletePlan(DeletePlanRequest request);
    Plan restorePlan(RestorePlanRequest request);
    List<Plan> getAssignedTeamPlansFromNowOn(UUID userId, UUID teamId);

    List<Plan> getTeamPlans(UUID teamId);
    List<Plan> getAllExpiredPlans();

    boolean existsById(UUID id);
    boolean isTeamPlan(UUID planId);

    void validateRemindTimesList(UUID planId, List<String> remindTime);
    void updateProgress(UUID id, float progress);
    void delete(Plan plan);

    String getPlanName(UUID planId);
    String getPlanEndAt(UUID planId);
    float getPlanProgress(UUID planId);
}
