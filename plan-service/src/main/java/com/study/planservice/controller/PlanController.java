package com.study.planservice.controller;

import com.study.planservice.entity.Plan;
import com.study.planservice.entity.PlanReminder;
import com.study.planservice.entity.Task;
import com.study.planservice.mapper.PlanMapper;
import com.study.planservice.mapper.PlanReminderMapper;
import com.study.planservice.mapper.TaskMapper;
import com.study.planservice.service.PlanNotificationService;
import com.study.planservice.service.PlanReminderService;
import com.study.planservice.service.PlanService;
import com.study.planservice.service.TaskService;
import com.study.common.grpc.ActionResponse;
import com.study.planservice.grpc.*;
import io.grpc.stub.StreamObserver;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
        List<Plan> plans = planService.getAssignedPlansOnDate(request);
        PlansResponse response = PlanMapper.toPlansResponse(plans);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getTeamPlansOnDate(GetTeamPlansOnDateRequest request, StreamObserver<TeamPlansResponse> responseObserver) {
        UUID userId = UUID.fromString(request.getUserId());
        List<Plan> plans = planService.getTeamPlansOnDate(request);
        TeamPlansResponse response = filterAssignedPlans(userId, plans);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getDatesWithDeadlineInMonth(GetDatesWithDeadlineInMonthRequest request, StreamObserver<DatesResponse> responseObserver) {
        Set<LocalDate> dates = planService.getDatesWithDeadlineInMonth(request);
        List<String> dateStrings = dates.stream().map(LocalDate::toString).toList();

        DatesResponse response = DatesResponse.newBuilder()
                .addAllDates(dateStrings)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getPersonalMissedPlans(GetPersonalMissedPlansRequest request, StreamObserver<PlansResponse> responseObserver) {
        List<Plan> plans = planService.getPersonalMissedPlans(request);
        PlansResponse response = PlanMapper.toPlansResponse(plans);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getTeamMissedPlans(GetTeamMissedPlansRequest request, StreamObserver<TeamPlansResponse> responseObserver) {
        UUID userId = UUID.fromString(request.getUserId());
        List<Plan> plans = planService.getTeamMissedPlans(request);
        TeamPlansResponse response = filterAssignedPlans(userId, plans);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updatePlan(UpdatePlanRequest request, StreamObserver<PlanResponse> responseObserver) {
        UUID planId = UUID.fromString(request.getId());
        LocalDateTime oldEndAt = LocalDateTime.parse(planService.getPlanEndAt(planId));

        Plan plan = planService.updatePlan(request);

        //Update expire reminder if plan deadline is changed
        if(!request.getEndAt().isEmpty()) {
            LocalDateTime newEndAt = plan.getEndAt();
            planReminderService.updateRemindTime(planId, oldEndAt, newEndAt);
        }

        //Notify assignees if update team plan
        if(plan.getTeamId() != null) {
            UUID userId = UUID.fromString(request.getUserId());
            List<UUID> assigneeIds = taskService.getAllAssigneeForPlan(planId);
            planNotificationService.publishPlanUpdatedNotification(
                    userId, planId, plan.getName(), assigneeIds
            );
        }

        PlanResponse response = PlanMapper.toPlanResponse(plan);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    @Transactional
    public void deletePlan(DeletePlanRequest request, StreamObserver<ActionResponse> responseObserver) {
        UUID planId = UUID.fromString(request.getId());

        //Notify assignees if delete team plan
        if (planService.isTeamPlan(planId)) {
            UUID userId = UUID.fromString(request.getUserId());
            String planName = planService.getPlanName(planId);
            List<UUID> assigneeIds = taskService.getAllAssigneeForPlan(planId);
            planNotificationService.publishPlanDeletedNotification(userId, planName, assigneeIds);
        }

        planService.deletePlan(request);
        planReminderService.deleteAllByPlanId(planId);
        taskService.deleteAllByPlanId(planId);

        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Delete plan successfully.")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void restorePlan(RestorePlanRequest request, StreamObserver<ActionResponse> responseObserver) {
        UUID planId = UUID.fromString(request.getId());

        Plan plan = planService.restorePlan(request);

        //Create expiry reminder
        List<UUID> assigneeIds = taskService.getAllAssigneeForPlan(planId);
        planReminderService.createPlanReminder(planId, plan.getEndAt(), assigneeIds);

        //Notify assignees if restore team plan
        if(planService.isTeamPlan(planId)) {
            UUID userId = UUID.fromString(request.getUserId());
            planNotificationService.publishPlanRestoredNotification(
                userId, planId, plan.getName(), assigneeIds
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
    public void isAssignedForTeamPlansFromNowOn(IsAssignedForTeamPlansFromNowOnRequest request, StreamObserver<ActionResponse> responseObserver) {
        UUID userId = UUID.fromString(request.getUserId());
        UUID teamId = UUID.fromString(request.getTeamId());

        List<Plan> plans = planService.getAssignedTeamPlansFromNowOn(userId, teamId);
        List<String> names = plans.stream().map(Plan::getName).toList();

        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(!plans.isEmpty())
                .setMessage("Assigned plans: " + String.join(",", names))
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

        Set<UUID> taskAssigneeIds = taskService.createTasks(request);
        List<UUID> planAssigneeIds = taskService.getAllAssigneeForPlan(planId);

        //Expiry reminder for newly created plan
        if(progress == 0){
            LocalDateTime remindAt = LocalDateTime.parse(planService.getPlanEndAt(planId));
            planReminderService.createPlanReminder(planId, remindAt, planAssigneeIds);
        }

        //Notify assignees if create/update team plan
        if(planService.isTeamPlan(planId)) {
            UUID userId = UUID.fromString(request.getUserId());
            String planName = planService.getPlanName(planId);

            //If plan is updated
            if(progress != 0) {
                //Update expiry reminder receivers, if plan is added with tasks
                planReminderService.updateReceiversForAllPlanReminders(planId, planAssigneeIds);
                //Notify assignees about the update
                planNotificationService.publishPlanUpdatedNotification(
                        userId, planId, planName, planAssigneeIds
                );
            }

            //Notify task assignees
            planNotificationService.publishPlanAssignedNotification(
                    planId, planName, new ArrayList<>(taskAssigneeIds)
            );

            //Completed plan + more tasks -> Incomplete plan
            if(progress == 1) {
                planNotificationService.publishPlanIncompleteNotification(
                        userId, planId, planName, planAssigneeIds
                );
            }
        }

        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Create tasks successfully.")
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
    public void updateTasksStatus(UpdateTasksStatusRequest request, StreamObserver<ActionResponse> responseObserver) {
        UUID planId = UUID.fromString(request.getPlanId());
        float oldProgress = planService.getPlanProgress(planId);

        taskService.updateTasksStatus(request);

        //Notify assignees if update team plan
        if(planService.isTeamPlan(planId)) {
            float newProgress = planService.getPlanProgress(planId);
            String planName = planService.getPlanName(planId);
            List<UUID> assigneeIds = taskService.getAllAssigneeForPlan(planId);

            //If all tasks status are updated to completed
            if(newProgress == 1) {
                planNotificationService.publishPlanCompletedNotification(
                        planId, planName, assigneeIds
                );
            }

            //If after updating, completed plan become incomplete
            if(oldProgress == 1 && newProgress < 1) {
                UUID userId = UUID.fromString(request.getUserId());
                planNotificationService.publishPlanIncompleteNotification(
                        userId, planId, planName, assigneeIds
                );
            }
        }

        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Update tasks status successfully.")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateTasksAssignee(UpdateTasksAssigneeRequest request, StreamObserver<ActionResponse> responseObserver) {
        UUID planId = UUID.fromString(request.getPlanId());
        float oldProgress = planService.getPlanProgress(planId);
        List<UUID> oldPlanAssigneeIds = taskService.getAllAssigneeForPlan(planId);

        Set<UUID> assigneeIds = taskService.updateTasksAssignee(request);

        //Notify assignees if update team plan
        if(planService.isTeamPlan(planId)) {
            UUID userId = UUID.fromString(request.getUserId());
            float newProgress = planService.getPlanProgress(planId);
            String planName = planService.getPlanName(planId);
            List<UUID> taskAssigneeIds = new ArrayList<>(assigneeIds);
            List<UUID> newPlanAssigneeIds = taskService.getAllAssigneeForPlan(planId);

            //Notify old assignees about the update
            planNotificationService.publishPlanUpdatedNotification(
                    userId, planId, planName, oldPlanAssigneeIds
            );

            //Notify task assignees
            planNotificationService.publishPlanAssignedNotification(
                    planId, planName, taskAssigneeIds
            );

            //Update reminders receivers
            planReminderService.updateReceiversForAllPlanReminders(planId, newPlanAssigneeIds);

            //If after updating, completed plan become incomplete
            if(oldProgress == 1 && newProgress < 1) {
                planNotificationService.publishPlanIncompleteNotification(
                        userId, planId, planName, newPlanAssigneeIds
                );
            }
        }

        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Update tasks assignee successfully.")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void deleteTasks(DeleteTasksRequest request, StreamObserver<ActionResponse> responseObserver) {
        UUID planId = UUID.fromString(request.getPlanId());

        taskService.deleteTasks(request);

        //Notify assignees if update team plan
        if(planService.isTeamPlan(planId)) {
            float progress = planService.getPlanProgress(planId);
            UUID userId = UUID.fromString(request.getUserId());
            String planName = planService.getPlanName(planId);
            List<UUID> assigneeIds = taskService.getAllAssigneeForPlan(planId);

            //Notify assignees about the update
            planNotificationService.publishPlanUpdatedNotification(
                userId, planId, planName, assigneeIds
            );

            //Update reminders receivers
            planReminderService.updateReceiversForAllPlanReminders(planId, assigneeIds);

            //If delete all incomplete plan -> completed plan
            if(progress == 1) {
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
        UUID planId = UUID.fromString(request.getPlanId());
        List<UUID> assigneeIds = taskService.getAllAssigneeForPlan(planId);

        planReminderService.createPlanReminders(assigneeIds, request);

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
