package com.nitb.planservice.service;

import com.nitb.planservice.entity.Plan;
import com.study.planservice.grpc.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface PlanService {
    Plan createPlan(CreatePlanRequest request);
    Plan getPlanById(GetPlanByIdRequest request);
    Plan updatePlan(UpdatePlanRequest request);
    void deletePlan(DeletePlanRequest request);
    void restorePlan(RestorePlanRequest request);

    List<Plan> getPlansOnDate(String dateStr, List<Plan> plans);
    List<LocalDate> getDatesWithDeadlineInMonth(List<Plan> plans , GetDatesWithDeadlineInMonthRequest request);
    List<Plan> getMissedPlans(List<Plan> plans);

    List<Plan> getTeamPlans(UUID teamId);
    List<Plan> getPersonalPlans(UUID userId);
    List<Plan> getPlansByListOfIds(List<UUID> ids);
}
