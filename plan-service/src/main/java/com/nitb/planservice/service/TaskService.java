package com.nitb.planservice.service;

import com.nitb.planservice.entity.Plan;
import com.nitb.planservice.entity.Task;
import com.study.planservice.grpc.CreateTasksRequest;
import com.study.planservice.grpc.DeleteTasksRequest;
import com.study.planservice.grpc.GetAllTasksInPlanRequest;
import com.study.planservice.grpc.UpdateTasksRequest;

import java.util.List;
import java.util.UUID;

public interface TaskService {
    void createTasks(CreateTasksRequest request);
    List<Task> getAllTasksInPlan(GetAllTasksInPlanRequest request);
    void updateTasks(UpdateTasksRequest request);
    void deleteTasks(DeleteTasksRequest request);

    List<Plan> getAssignedPlans(UUID userId);
    boolean isAssignedForPlan(UUID userId, UUID planId);
}
