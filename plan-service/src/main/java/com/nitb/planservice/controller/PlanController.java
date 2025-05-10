package com.nitb.planservice.controller;

import com.nitb.planservice.entity.Plan;
import com.nitb.planservice.mapper.PlanMapper;
import com.nitb.planservice.service.PlanService;
import com.nitb.planservice.service.TaskService;
import com.study.common.grpc.ActionResponse;
import com.study.planservice.grpc.*;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@GrpcService
@RequiredArgsConstructor
public class PlanController extends PlanServiceGrpc.PlanServiceImplBase {
    private final PlanService planService;
    private final TaskService taskService;

    //Plans
    @Override
    public void createPlan(CreatePlanRequest request, StreamObserver<PlanResponse> responseObserver) {
        Plan plan = planService.createPlan(request);
        PlanResponse response = PlanMapper.toPlanResponse(plan);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getPlanById(GetPlanByIdRequest request, StreamObserver<PlanDetailResponse> responseObserver) {
        Plan plan = planService.getPlanById(request);
        PlanDetailResponse response = PlanMapper.toPlanDetailResponse(plan);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getAssignedPlansOnDate(GetAssignedPlansOnDateRequest request, StreamObserver<PlansResponse> responseObserver) {
        UUID userId = UUID.fromString(request.getUserId());

        List<Plan> plans = taskService.getAssignedPlans(userId);
        List<Plan> filteredPlans = planService.getPlansOnDate(request.getDate(), plans);

        PlansResponse response = PlanMapper.toPlansResponse(filteredPlans);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getTeamPlansOnDate(GetTeamPlansOnDateRequest request, StreamObserver<TeamPlansResponse> responseObserver) {
        UUID userId = UUID.fromString(request.getUserId());
        UUID teamId = UUID.fromString(request.getTeamId());

        List<Plan> teamPlans = planService.getTeamPlans(teamId);
        List<Plan> filteredPlans = planService.getPlansOnDate(request.getDate(), teamPlans);

        TeamPlansResponse response = filterAssignedPlans(userId, filteredPlans);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getDatesWithDeadlineInMonth(GetDatesWithDeadlineInMonthRequest request, StreamObserver<DatesResponse> responseObserver) {
        List<Plan> plans = request.getTeamId().isEmpty() ?
                taskService.getAssignedPlans(UUID.fromString(request.getUserId())) :
                planService.getTeamPlans(UUID.fromString(request.getTeamId()));

        List<LocalDate> dates = planService.getDatesWithDeadlineInMonth(plans, request);
        List<String> dateStrings = dates.stream().map(LocalDate::toString).toList();

        DatesResponse response = DatesResponse.newBuilder()
                .addAllDates(dateStrings)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getPersonalMissedPlans(GetPersonalMissedPlansRequest request, StreamObserver<PlansResponse> responseObserver) {
        UUID userId = UUID.fromString(request.getUserId());

        List<Plan> personalPlans = planService.getPersonalPlans(userId);
        List<Plan> filteredPlans = planService.getMissedPlans(personalPlans);

        PlansResponse response = PlanMapper.toPlansResponse(filteredPlans);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getTeamMissedPlans(GetTeamMissedPlansRequest request, StreamObserver<TeamPlansResponse> responseObserver) {
        UUID userId = UUID.fromString(request.getUserId());
        UUID teamId = UUID.fromString(request.getTeamId());

        List<Plan> teamPlans = planService.getTeamPlans(teamId);
        List<Plan> filteredPlans = planService.getMissedPlans(teamPlans);

        TeamPlansResponse response = filterAssignedPlans(userId, filteredPlans);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updatePlan(UpdatePlanRequest request, StreamObserver<PlanResponse> responseObserver) {
        Plan plan = planService.updatePlan(request);
        PlanResponse response = PlanMapper.toPlanResponse(plan);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void deletePlan(DeletePlanRequest request, StreamObserver<ActionResponse> responseObserver) {
        planService.deletePlan(request);

        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Delete plan successfully")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void restorePlan(RestorePlanRequest request, StreamObserver<ActionResponse> responseObserver) {
        planService.restorePlan(request);

        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Restore plan successfully")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private TeamPlansResponse filterAssignedPlans(UUID userId, List<Plan> plans) {
        List<TeamPlanSummaryResponse> filteredPlans = new ArrayList<>();

        for(Plan plan : plans) {
            boolean isAssigned = taskService.isAssignedForPlan(userId, plan.getId());
            TeamPlanSummaryResponse summary = PlanMapper.toTeamPlanSummaryResponse(plan, isAssigned);
            filteredPlans.add(summary);
        }

        return PlanMapper.toTeamPlansResponse(filteredPlans);
    }
}
