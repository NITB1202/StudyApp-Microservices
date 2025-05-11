package com.nitb.planservice.service.impl;

import com.nitb.planservice.entity.Plan;
import com.nitb.planservice.repository.PlanRepository;
import com.nitb.planservice.service.PlanService;
import com.study.common.exceptions.BusinessException;
import com.study.common.exceptions.NotFoundException;
import com.study.planservice.grpc.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService {
    private final PlanRepository planRepository;
    private final int EXPIRE_DAYS = 3;

    @Override
    public Plan createPlan(CreatePlanRequest request) {
        LocalDateTime startAt = LocalDateTime.parse(request.getStartAt());
        LocalDateTime endAt = LocalDateTime.parse(request.getEndAt());

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
    public List<Plan> getPlansOnDate(String dateStr, List<Plan> plans) {
        LocalDate date = LocalDate.parse(dateStr);
        LocalDateTime dateTime = date.atStartOfDay();

        List<Plan> result = new ArrayList<>();

        for(Plan plan : plans) {
            if(dateTime.isAfter(plan.getStartAt()) && dateTime.isBefore(plan.getEndAt())) {
                result.add(plan);
            }
        }

        return result;
    }

    @Override
    public List<LocalDate> getDatesWithDeadlineInMonth(List<Plan> plans , GetDatesWithDeadlineInMonthRequest request) {
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

        List<LocalDate> result = new ArrayList<>();

        for(Plan plan : plans) {
            LocalDateTime deadline = plan.getEndAt();
            if(deadline.isAfter(startOfMonth) && deadline.isBefore(endOfMonth)){
                result.add(deadline.toLocalDate());
            }

        }

        return result;
    }

    @Override
    public List<Plan> getMissedPlans(List<Plan> plans) {
        List<Plan> result = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        for(Plan plan : plans) {
            LocalDateTime expireAt = (plan.getEndAt().plusDays(EXPIRE_DAYS)).with(LocalTime.MAX);
            if(now.isAfter(plan.getEndAt()) && now.isBefore(expireAt) ) {
                result.add(plan);
            }
        }

        return result;
    }

    @Override
    public List<Plan> getTeamPlans(UUID teamId) {
        return planRepository.findByTeamId(teamId);
    }

    @Override
    public List<Plan> getPersonalPlans(UUID userId) {
        return planRepository.findByCreatorIdAndTeamIdIsNull(userId);
    }

    @Override
    public List<Plan> getPlansByListOfIds(List<UUID> ids) {
        return planRepository.findAllById(ids);
    }

    @Override
    public List<Plan> getAllExpiredPlans() {
        //Plan expire when endAt + 3days < now -> endAt < now - 3days
        LocalDateTime now = LocalDateTime.now();
        return planRepository.findAllByCompleteAtNullAndEndAtBefore(now.minusDays(EXPIRE_DAYS));
    }

    @Override
    public boolean existsById(UUID id) {
        return planRepository.existsById(id);
    }

    @Override
    public boolean isPlanCompleted(UUID id) {
        Plan plan = planRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Plan not found.")
        );

        return plan.getCompleteAt() != null;
    }

    @Override
    public void updateProgress(UUID id, float progress) {
        Plan plan = planRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Plan not found.")
        );

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
    public Plan updatePlan(UpdatePlanRequest request) {
        UUID planId = UUID.fromString(request.getId());

        Plan plan = planRepository.findById(planId).orElseThrow(
                () -> new NotFoundException("Plan not found.")
        );

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
                throw new BusinessException("End date must be before start date.");
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
    public void restorePlan(RestorePlanRequest request) {
        UUID planId = UUID.fromString(request.getId());

        Plan plan = planRepository.findById(planId).orElseThrow(
                () -> new NotFoundException("Plan not found.")
        );

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expireAt = (plan.getEndAt().plusDays(EXPIRE_DAYS)).with(LocalTime.MAX);

        if(now.isAfter(plan.getEndAt()) && now.isBefore(expireAt)) {
            LocalDateTime newDeadline = LocalDateTime.parse(request.getEndAt());

            if(!newDeadline.isAfter(now)) {
                throw new BusinessException("End date must be after now.");
            }

            plan.setEndAt(newDeadline);
            planRepository.save(plan);
        }
        else {
            if(now.isAfter(expireAt)) {
                throw new BusinessException("Plan has expired.");
            }

            throw new BusinessException("Plan is still ongoing.");
        }
    }
}
