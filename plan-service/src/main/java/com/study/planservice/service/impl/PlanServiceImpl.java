package com.study.planservice.service.impl;

import com.study.planservice.entity.Plan;
import com.study.planservice.repository.PlanRepository;
import com.study.planservice.service.PlanService;
import com.study.common.exceptions.BusinessException;
import com.study.common.exceptions.NotFoundException;
import com.study.planservice.grpc.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService {
    private final PlanRepository planRepository;
    private final int EXPIRE_DAYS = 3;

    @Override
    public Plan createPlan(CreatePlanRequest request) {
        LocalDateTime startAt = LocalDateTime.parse(request.getStartAt());
        LocalDateTime endAt = LocalDateTime.parse(request.getEndAt());

        if(endAt.isBefore(LocalDateTime.now())) {
            throw new BusinessException("Can't create a plan that ends in the past.");
        }

        if(startAt.isAfter(endAt)) {
            throw new BusinessException("Start date must be before end date.");
        }

        if(request.getName().isEmpty()){
            throw new BusinessException("Name cannot be empty.");
        }

        UUID teamId = request.getTeamId().isEmpty() ? null : UUID.fromString(request.getTeamId());

        Plan plan = Plan.builder()
                .creatorId(UUID.fromString(request.getUserId()))
                .name(request.getName())
                .description(request.getDescription())
                .startAt(startAt)
                .endAt(endAt)
                .progress(0f)
                .teamId(teamId)
                .build();

        return planRepository.save(plan);
    }

    @Override
    public Plan getPlanById(GetPlanByIdRequest request) {
        UUID planId = UUID.fromString(request.getId());
        return planRepository.findById(planId).orElseThrow(
                () -> new NotFoundException("Plan not found.")
        );
    }

    @Override
    public List<Plan> getAssignedPlansOnDate(GetAssignedPlansOnDateRequest request) {
        LocalDate date = LocalDate.parse(request.getDate());
        LocalDateTime startOfDate = date.atStartOfDay();
        LocalDateTime endOfDate = date.atTime(LocalTime.MAX);

        UUID userId = UUID.fromString(request.getUserId());

        return planRepository.findAssignedPlansByUserIdAndInDate(userId, startOfDate, endOfDate);
    }

    @Override
    public List<Plan> getTeamPlansOnDate(GetTeamPlansOnDateRequest request) {
        LocalDate date = LocalDate.parse(request.getDate());
        LocalDateTime startOfDate = date.atStartOfDay();
        LocalDateTime endOfDate = date.atTime(LocalTime.MAX);

        UUID teamId = UUID.fromString(request.getTeamId());

        return planRepository.findTeamPlansByTeamIdAndInDate(teamId, startOfDate, endOfDate);
    }

    @Override
    public Set<LocalDate> getDatesWithDeadlineInMonth(GetDatesWithDeadlineInMonthRequest request) {
        int month = request.getMonth();
        int year = request.getYear();

        if(month < 1 || month > 12) {
            throw new BusinessException("Month must be between 1 and 12.");
        }

        if(year <= 0) {
            throw new BusinessException("Year must be greater than 0.");
        }

        LocalDateTime startOfMonth = LocalDate.of(year, month, 1).atStartOfDay();
        LocalDateTime endOfMonth = LocalDate.of(year, month, 1)
                .withDayOfMonth(LocalDate.of(year, month, 1).lengthOfMonth())
                .atTime(LocalTime.MAX);

        List<Plan> plans = new ArrayList<>();

        if(request.getTeamId().isEmpty()) {
            //Get assigned plans for the month
            UUID userId = UUID.fromString(request.getUserId());
            List<Plan> assignedPlans = planRepository.findAssignedPlansByUserIdAndEndAtBetween(userId, startOfMonth, endOfMonth);
            plans.addAll(assignedPlans);
        }
        else {
            //Get team plans for the month
            UUID teamId = UUID.fromString(request.getTeamId());
            List<Plan> teamPlans = planRepository.findPlansByTeamIdAndEndAtBetween(teamId, startOfMonth, endOfMonth);
            plans.addAll(teamPlans);
        }

        return plans.stream().map(plan -> plan.getEndAt().toLocalDate()).collect(Collectors.toSet());
    }

    @Override
    public List<Plan> getPersonalMissedPlans(GetPersonalMissedPlansRequest request) {
        UUID userId = UUID.fromString(request.getUserId());
        LocalDateTime now = LocalDateTime.now();
        return planRepository.findPlansByCreatorIdAndTeamIdIsNullAndCompleteAtIsNullAndEndAtBefore(userId, now);
    }

    @Override
    public List<Plan> getTeamMissedPlans(GetTeamMissedPlansRequest request) {
        UUID teamId = UUID.fromString(request.getTeamId());
        LocalDateTime now = LocalDateTime.now();
        return planRepository.findPlansByTeamIdAndCompleteAtIsNullAndEndAtBefore(teamId, now);
    }

    @Override
    public Plan updatePlan(UpdatePlanRequest request) {
        UUID planId = UUID.fromString(request.getId());
        LocalDateTime now = LocalDateTime.now();

        Plan plan = planRepository.findById(planId).orElseThrow(
                () -> new NotFoundException("Plan not found.")
        );

        if(plan.getEndAt().isBefore(now)){
            throw new BusinessException("Plan has expired.");
        }

        if(!request.getName().isEmpty()) {
            plan.setName(request.getName());
        }

        if(!request.getDescription().isEmpty()) {
            plan.setDescription(request.getDescription());
        }

        if(!request.getStartAt().isEmpty()) {
            LocalDateTime startAt = LocalDateTime.parse(request.getStartAt());
            if(startAt.isAfter(plan.getEndAt())) {
                throw new BusinessException("Start date must be before end date.");
            }
            plan.setStartAt(startAt);
        }

        if(!request.getEndAt().isEmpty()) {
            LocalDateTime endAt = LocalDateTime.parse(request.getEndAt());
            if(endAt.isBefore(plan.getStartAt())) {
                throw new BusinessException("End date must be after start date.");
            }
            if(endAt.isBefore(now)) {
                throw new BusinessException("End date can't be in the past.");
            }
            plan.setEndAt(endAt);
        }

        return planRepository.save(plan);
    }

    @Override
    public void deletePlan(DeletePlanRequest request) {
        UUID planId = UUID.fromString(request.getId());

        Plan plan = planRepository.findById(planId).orElseThrow(
                () -> new NotFoundException("Plan not found.")
        );

        planRepository.delete(plan);
    }

    @Override
    public Plan restorePlan(RestorePlanRequest request) {
        UUID planId = UUID.fromString(request.getId());

        Plan plan = planRepository.findById(planId).orElseThrow(
                () -> new NotFoundException("Plan not found.")
        );

        LocalDateTime now = LocalDateTime.now();

        if(plan.getCompleteAt() == null && now.isAfter(plan.getEndAt())) {
            LocalDateTime newDeadline = LocalDateTime.parse(request.getEndAt());

            if(!newDeadline.isAfter(now)) {
                throw new BusinessException("End date must be after now.");
            }

            plan.setEndAt(newDeadline);
            planRepository.save(plan);

            return plan;
        }
        else {
            throw new BusinessException("Plan is still ongoing.");
        }
    }

    @Override
    public long getWeeklyFinishedPlansCount(GetWeeklyPlanStatsRequest request) {
        UUID userId = UUID.fromString(request.getUserId());

        LocalDateTime startOfWeek = LocalDate.now()
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                .atStartOfDay();

        LocalDateTime endOfWeek = LocalDate.now()
                    .with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
                    .atTime(LocalTime.MAX);


        return planRepository.countCompletedAssignedPlansByUserIdAndIn(userId, startOfWeek, endOfWeek);
    }

    @Override
    public float getWeeklyFinishedAssignedPlansPercentage(GetWeeklyPlanStatsRequest request) {
        UUID userId = UUID.fromString(request.getUserId());

        LocalDateTime startOfWeek = LocalDate.now()
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                .atStartOfDay();

        LocalDateTime endOfWeek = LocalDate.now()
                .with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
                .atTime(LocalTime.MAX);

        long totalPlans = planRepository.countAssignedPlansHaveDeadlineIn(userId, startOfWeek, endOfWeek);

        if(totalPlans == 0) return 1;

        long completedPlans = planRepository.countCompletedAssignedPlansHaveDeadlineIn(userId, startOfWeek, endOfWeek);

        return (float)completedPlans / totalPlans;
    }

    @Override
    public List<Plan> getAssignedTeamPlansFromNowOn(UUID userId, UUID teamId) {
        return planRepository.findAssignedTeamPlansFromNowOn(userId, teamId, LocalDateTime.now());
    }

    @Override
    public List<Plan> getAllTeamPlans(UUID teamId) {
        return planRepository.findByTeamId(teamId);
    }

    @Override
    public List<Plan> getAllExpiredPlans() {
        //Plan expire when endAt + 3days < now -> endAt < now - 3days
        LocalDateTime now = LocalDateTime.now();
        return planRepository.findAllByCompleteAtNullAndEndAtBefore(now.minusDays(EXPIRE_DAYS));
    }

    @Override
    public boolean isTeamPlan(UUID planId) {
        Plan plan = planRepository.findById(planId).orElseThrow(
                () -> new NotFoundException("Plan not found.")
        );

        return plan.getTeamId() != null;
    }

    @Override
    public void validateUpdatePlanRequest(UUID planId) {
        Plan plan = planRepository.findById(planId).orElseThrow(
                () -> new NotFoundException("Plan not found.")
        );

        if(plan.getEndAt().isBefore(LocalDateTime.now())){
            throw new BusinessException("Plan has expired.");
        }
    }

    @Override
    public void validateRemindTimesList(UUID planId, List<String> remindTime) {
        LocalDateTime now = LocalDateTime.now();

        Plan plan = planRepository.findById(planId).orElseThrow(
                ()-> new NotFoundException("Plan not found.")
        );

        if(plan.getEndAt().isBefore(LocalDateTime.now())){
            throw new BusinessException("Plan has expired.");
        }

        for(String time : remindTime){
            LocalDateTime remindAt = LocalDateTime.parse(time);

            if(remindAt.isBefore(plan.getStartAt()) || remindAt.isAfter(plan.getEndAt())) {
                throw new BusinessException("Invalid remind time.");
            }

            if(remindAt.isBefore(now)) {
                throw new BusinessException("Can't set a reminder for the past.");
            }
        }
    }

    @Override
    public void updateProgress(UUID id, float progress) {
        Plan plan = planRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Plan not found.")
        );

        if(progress < 0 || progress > 1){
            throw new BusinessException("Invalid progress.");
        }

        plan.setProgress(progress);

        if(progress == 1) {
            plan.setCompleteAt(LocalDateTime.now());
        }
        else {
            plan.setCompleteAt(null);
        }

        planRepository.save(plan);
    }

    @Override
    public void delete(Plan plan) {
        planRepository.delete(plan);
    }

    @Override
    public String getPlanName(UUID planId) {
        Plan plan = planRepository.findById(planId).orElseThrow(
                () -> new NotFoundException("Plan not found.")
        );

        return plan.getName();
    }

    @Override
    public LocalDateTime getPlanEndAt(UUID planId) {
        Plan plan = planRepository.findById(planId).orElseThrow(
                () -> new NotFoundException("Plan not found.")
        );

        return plan.getEndAt();
    }

    @Override
    public float getPlanProgress(UUID planId) {
        Plan plan = planRepository.findById(planId).orElseThrow(
                () -> new NotFoundException("Plan not found.")
        );

        return plan.getProgress();
    }

    @Override
    public UUID getTeamId(UUID planId) {
        Plan plan = planRepository.findById(planId).orElseThrow(
                () -> new NotFoundException("Plan not found.")
        );

        return plan.getTeamId();
    }
}
