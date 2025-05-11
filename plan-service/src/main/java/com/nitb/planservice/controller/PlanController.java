package com.nitb.planservice.controller;

import com.nitb.planservice.entity.Plan;
import com.nitb.planservice.entity.PlanReminder;
import com.nitb.planservice.entity.Task;
import com.nitb.planservice.mapper.PlanMapper;
import com.nitb.planservice.mapper.PlanReminderMapper;
import com.nitb.planservice.mapper.TaskMapper;
import com.nitb.planservice.service.PlanNotificationService;
import com.nitb.planservice.service.PlanReminderService;
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
import java.util.Set;
import java.util.UUID;

@GrpcService
@RequiredArgsConstructor
public class PlanController extends PlanServiceGrpc.PlanServiceImplBase {
    private final PlanService planService;
    private final TaskService taskService;
    private final PlanReminderService planReminderService;
    private final PlanNotificationService planNotificationService;

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

        //Notification if update team plan
        if(plan.getTeamId() != null) {
            UUID userId = UUID.fromString(request.getUserId());
            List<UUID> assigneeIds = taskService.getAllAssigneeForPlan(plan.getId());
            planNotificationService.publishPlanUpdatedNotification(
                    userId, plan.getId(), plan.getName(), assigneeIds
            );
        }

        PlanResponse response = PlanMapper.toPlanResponse(plan);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void deletePlan(DeletePlanRequest request, StreamObserver<ActionResponse> responseObserver) {
        UUID planId = UUID.fromString(request.getId());

        //Notification if delete team plan
        if (planService.isTeamPlan(planId)) {
            String planName = planService.getPlanName(planId);
            List<UUID> assigneeIds = taskService.getAllAssigneeForPlan(planId);
            planNotificationService.publishPlanDeletedNotification(planName, assigneeIds);
        }

        planService.deletePlan(request);
        taskService.deleteAllByPlanId(planId);

        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Delete plan successfully")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void restorePlan(RestorePlanRequest request, StreamObserver<ActionResponse> responseObserver) {
        UUID planId = UUID.fromString(request.getId());

        planService.restorePlan(request);

        //Notification if restore team plan
        if(planService.isTeamPlan(planId)) {
            UUID userId = UUID.fromString(request.getUserId());
            String planName = planService.getPlanName(planId);
            List<UUID> assigneeIds = taskService.getAllAssigneeForPlan(planId);
            planNotificationService.publishPlanRestoredNotification(
                userId, planId, planName, assigneeIds
            );
        }

        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Restore plan successfully")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void isAssignedForOngoingPlan(IsAssignedForOngoingPlanRequest request, StreamObserver<ActionResponse> responseObserver) {
        UUID teamId = UUID.fromString(request.getTeamId());
        UUID userId = UUID.fromString(request.getUserId());

        List<Plan> teamOngoingPlans = planService.getTeamOngoingPlan(teamId);
        List<String> assignedPlan = new ArrayList<>();

        for(Plan plan : teamOngoingPlans) {
            if(taskService.isAssignedForPlan(userId, plan.getId())){
                assignedPlan.add(plan.getName());
            }
        }

        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(!assignedPlan.isEmpty())
                .setMessage("Assigned plans: " + String.join(",", assignedPlan))
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

    //Tasks
    @Override
    public void createTasks(CreateTasksRequest request, StreamObserver<ActionResponse> responseObserver) {
        UUID planId = UUID.fromString(request.getPlanId());
        float progress = planService.getPlanProgress(planId);

        Set<UUID> assigneeIds = taskService.createTasks(request);

        //Notification if create/update teamPlan
        if(planService.isTeamPlan(planId)) {
            String planName = planService.getPlanName(planId);
            planNotificationService.publishPlanAssignedNotification(
                    planId, planName, new ArrayList<>(assigneeIds)
            );

            if(progress == 1) {
                UUID userId = UUID.fromString(request.getUserId());
                planNotificationService.publishPlanIncompleteNotification(
                        userId, planId, planName, taskService.getAllAssigneeForPlan(planId)
                );
            }
        }

        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Create tasks successfully")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getAllTasksInPlan(GetAllTasksInPlanRequest request, StreamObserver<TasksResponse> responseObserver) {
        List<Task> tasks = taskService.getAllTasksInPlan(request);
        TasksResponse response = TaskMapper.toTasksResponse(tasks);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateTasks(UpdateTasksRequest request, StreamObserver<ActionResponse> responseObserver) {
        UUID planId = UUID.fromString(request.getPlanId());
        float progress = planService.getPlanProgress(planId);

        taskService.updateTasks(request);

        //Notification if update team plan
        if(planService.isTeamPlan(planId)) {
            float newProgress = planService.getPlanProgress(planId);
            String planName = planService.getPlanName(planId);
            List<UUID> assigneeIds = taskService.getAllAssigneeForPlan(planId);

            if(newProgress == 1) {
                planNotificationService.publishPlanCompletedNotification(
                        planId, planName, assigneeIds
                );
            }

            if(progress == 1 && newProgress < 1) {
                UUID userId = UUID.fromString(request.getUserId());
                planNotificationService.publishPlanIncompleteNotification(
                        userId, planId, planName, assigneeIds
                );
            }
        }

        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Update tasks successfully")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void deleteTasks(DeleteTasksRequest request, StreamObserver<ActionResponse> responseObserver) {
        UUID planId = UUID.fromString(request.getPlanId());
        taskService.deleteTasks(request);

        //Notification if update team plan
        if(planService.isTeamPlan(planId)) {
            float progress = planService.getPlanProgress(planId);
            if(progress == 1) {
                String planName = planService.getPlanName(planId);
                List<UUID> assigneeIds = taskService.getAllAssigneeForPlan(planId);
                planNotificationService.publishPlanCompletedNotification(
                        planId, planName, assigneeIds
                );
            }
        }

        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Delete tasks successfully.")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    //PlanReminders
    @Override
    public void createPlanReminders(CreatePlanRemindersRequest request, StreamObserver<ActionResponse> responseObserver) {
        planReminderService.createPlanReminders(request);

        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Create reminders successfully.")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getAllPlanRemindersInPlan(GetAllPlanRemindersInPlanRequest request, StreamObserver<PlanRemindersResponse> responseObserver) {
        List<PlanReminder> reminders = planReminderService.getAllPlanRemindersInPlan(request);
        PlanRemindersResponse response = PlanReminderMapper.toPlanRemindersResponse(reminders);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updatePlanReminders(UpdatePlanRemindersRequest request, StreamObserver<ActionResponse> responseObserver) {
        planReminderService.updatePlanReminders(request);

        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Update reminders successfully.")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void deletePlanReminders(DeletePlanRemindersRequest request, StreamObserver<ActionResponse> responseObserver) {
        planReminderService.deletePlanReminders(request);

        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Delete reminders successfully.")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
