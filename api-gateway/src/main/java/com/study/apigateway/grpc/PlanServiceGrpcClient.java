package com.study.apigateway.grpc;

import com.study.apigateway.dto.Plan.Plan.request.CreatePlanRequestDto;
import com.study.apigateway.dto.Plan.Plan.request.RestorePlanRequestDto;
import com.study.apigateway.dto.Plan.Plan.request.UpdatePlanRequestDto;
import com.study.apigateway.dto.Plan.Task.request.CreateTaskRequestDto;
import com.study.apigateway.mapper.TaskMapper;
import com.study.common.grpc.ActionResponse;
import com.study.planservice.grpc.*;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PlanServiceGrpcClient {
    @GrpcClient("plan-service")
    private PlanServiceGrpc.PlanServiceBlockingStub planStub;

   //Plan
    public PlanResponse createPlan(UUID userId, UUID teamId, CreatePlanRequestDto dto) {
        String teamIdStr = teamId == null ? "" : teamId.toString();
        String description = dto.getDescription() == null ? "" : dto.getDescription().trim();

        CreatePlanRequest request = CreatePlanRequest.newBuilder()
                .setUserId(userId.toString())
                .setName(dto.getName())
                .setDescription(description)
                .setStartAt(dto.getStartAt().toString())
                .setEndAt(dto.getEndAt().toString())
                .setTeamId(teamIdStr)
                .build();

        return planStub.createPlan(request);
    }

    public PlanDetailResponse getPlanById(UUID planId) {
        GetPlanByIdRequest request = GetPlanByIdRequest.newBuilder()
                .setId(planId.toString())
                .build();

        return planStub.getPlanById(request);
    }

    public PlansResponse getAssignedPlansOnDate(UUID userId, LocalDate date) {
        GetAssignedPlansOnDateRequest request = GetAssignedPlansOnDateRequest.newBuilder()
                .setUserId(userId.toString())
                .setDate(date.toString())
                .build();

        return planStub.getAssignedPlansOnDate(request);
    }

    public TeamPlansResponse getTeamPlansOnDate(UUID userId, UUID teamId, LocalDate date) {
        GetTeamPlansOnDateRequest request = GetTeamPlansOnDateRequest.newBuilder()
                .setUserId(userId.toString())
                .setTeamId(teamId.toString())
                .setDate(date.toString())
                .build();

        return planStub.getTeamPlansOnDate(request);
    }

    public DatesResponse getDatesWithDeadlineInMonth(UUID userId, int month, int year, UUID teamId) {
        String teamIdStr = teamId == null ? "" : teamId.toString();

        GetDatesWithDeadlineInMonthRequest request = GetDatesWithDeadlineInMonthRequest.newBuilder()
                .setUserId(userId.toString())
                .setMonth(month)
                .setYear(year)
                .setTeamId(teamIdStr)
                .build();

        return planStub.getDatesWithDeadlineInMonth(request);
    }

    public ActionResponse isAssignedForTeamPlansFromNowOn(UUID userId, UUID teamId) {
        IsAssignedForTeamPlansFromNowOnRequest request = IsAssignedForTeamPlansFromNowOnRequest.newBuilder()
                .setUserId(userId.toString())
                .setTeamId(teamId.toString())
                .build();

        return planStub.isAssignedForTeamPlansFromNowOn(request);
    }

    public PlansResponse getPersonalMissedPlans(UUID userId) {
        GetPersonalMissedPlansRequest request = GetPersonalMissedPlansRequest.newBuilder()
                .setUserId(userId.toString())
                .build();

        return planStub.getPersonalMissedPlans(request);
    }

    public TeamPlansResponse getTeamMissedPlans(UUID userId, UUID teamId) {
        GetTeamMissedPlansRequest request = GetTeamMissedPlansRequest.newBuilder()
                .setUserId(userId.toString())
                .setTeamId(teamId.toString())
                .build();

        return planStub.getTeamMissedPlans(request);
    }

    public PlanResponse updatePlan(UUID userId, UUID planId, UpdatePlanRequestDto dto) {
        String name = dto.getName() == null ? "" : dto.getName().trim();
        String description = dto.getDescription() == null ? "" : dto.getDescription().trim();
        String startAt = dto.getStartAt() == null ? "" : dto.getStartAt().toString();
        String endAt = dto.getEndAt() == null ? "" : dto.getEndAt().toString();

        UpdatePlanRequest request = UpdatePlanRequest.newBuilder()
                .setUserId(userId.toString())
                .setId(planId.toString())
                .setName(name)
                .setDescription(description)
                .setStartAt(startAt)
                .setEndAt(endAt)
                .build();

        return planStub.updatePlan(request);
    }

    public ActionResponse deletePlan(UUID userId, UUID planId) {
        DeletePlanRequest request = DeletePlanRequest.newBuilder()
                .setUserId(userId.toString())
                .setId(planId.toString())
                .build();

        return planStub.deletePlan(request);
    }

    public ActionResponse restorePlan(UUID userId, UUID planId, RestorePlanRequestDto dto) {
        RestorePlanRequest request = RestorePlanRequest.newBuilder()
                .setUserId(userId.toString())
                .setId(planId.toString())
                .setEndAt(dto.getEndAt().toString())
                .build();

        return planStub.restorePlan(request);
    }

    //Task
    public ActionResponse createTasks(UUID userId, UUID planId, List<CreateTaskRequestDto> tasks) {
        List<CreateTaskRequest> createTaskRequests = tasks.stream().map(TaskMapper::toCreateTaskRequest).toList();

        CreateTasksRequest request = CreateTasksRequest.newBuilder()
                .setUserId(userId.toString())
                .setPlanId(planId.toString())
                .addAllTasks(createTaskRequests)
                .build();

        return planStub.createTasks(request);
    }

    public TasksResponse getAllTasksInPlan(UUID planId) {
        GetAllTasksInPlanRequest request = GetAllTasksInPlanRequest.newBuilder()
                .setPlanId(planId.toString())
                .build();

        return planStub.getAllTasksInPlan(request);
    }

    //Plan reminder
    public ActionResponse createPlanReminders(UUID planId, List<LocalDateTime> remindTimes) {
        List<String> timeStrings = remindTimes.stream().map(LocalDateTime::toString).toList();

        CreatePlanRemindersRequest request = CreatePlanRemindersRequest.newBuilder()
                .setPlanId(planId.toString())
                .addAllRemindTimes(timeStrings)
                .build();

        return planStub.createPlanReminders(request);
    }

    public PlanRemindersResponse getAllPlanRemindersInPlan(UUID planId) {
        GetAllPlanRemindersInPlanRequest request = GetAllPlanRemindersInPlanRequest.newBuilder()
                .setPlanId(planId.toString())
                .build();

        return planStub.getAllPlanRemindersInPlan(request);
    }
}
