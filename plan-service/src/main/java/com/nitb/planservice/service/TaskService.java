package com.nitb.planservice.service;

import com.nitb.planservice.entity.Task;
import com.study.planservice.grpc.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface TaskService {
    Set<UUID> createTasks(CreateTasksRequest request);
    List<Task> getAllTasksInPlan(GetAllTasksInPlanRequest request);
    void updateTasksStatus(UpdateTasksStatusRequest request);
    Set<UUID> updateTasksAssignee(UpdateTasksAssigneeRequest request);
    void deleteTasks(DeleteTasksRequest request);

    List<UUID> getAllAssigneeForPlan(UUID planId);
    boolean isAssignedForPlan(UUID userId, UUID planId);
    void deleteAllByPlanId(UUID planId);
}
