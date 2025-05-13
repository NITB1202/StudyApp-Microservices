package com.nitb.planservice.service.impl;

import com.nitb.planservice.entity.Task;
import com.nitb.planservice.repository.TaskRepository;
import com.nitb.planservice.service.PlanService;
import com.nitb.planservice.service.TaskService;
import com.study.common.exceptions.BusinessException;
import com.study.common.exceptions.NotFoundException;
import com.study.planservice.grpc.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final PlanService planService;

    @Override
    public Set<UUID> createTasks(CreateTasksRequest request) {
        UUID planId = UUID.fromString(request.getPlanId());

        planService.validateUpdatePlanRequest(planId);

        Set<Task> tasks = new LinkedHashSet<>();
        Set<String> names = new LinkedHashSet<>();
        Set<UUID> assignees = new LinkedHashSet<>();

        for(CreateTaskRequest createRequest : request.getTasksList()){
            String name = createRequest.getName();
            UUID assigneeId = UUID.fromString(createRequest.getAssigneeId());

            if(name.isEmpty()){
                throw new BusinessException("Name cannot be empty.");
            }

            if(!names.add(name) || taskRepository.existsByNameAndPlanId(name, planId)){
                throw new BusinessException("Duplicate task name.");
            }

            Task task = Task.builder()
                    .name(name)
                    .assigneeId(assigneeId)
                    .isCompleted(false)
                    .build();

            tasks.add(task);
            assignees.add(assigneeId);
        }

        taskRepository.saveAll(tasks);

        //Update progress
        float progress = calculateProgress(planId);
        planService.updateProgress(planId, progress);

        return assignees;
    }

    @Override
    public List<Task> getAllTasksInPlan(GetAllTasksInPlanRequest request) {
        UUID planId = UUID.fromString(request.getPlanId());
        return taskRepository.findByPlanId(planId);
    }

    @Override
    public void updateTasksStatus(UpdateTasksStatusRequest request) {
        UUID userId = UUID.fromString(request.getUserId());
        UUID planId = UUID.fromString(request.getPlanId());

        planService.validateUpdatePlanRequest(planId);

        for(UpdateTaskStatusRequest updateRequest : request.getRequestsList()){
            UUID taskId = UUID.fromString(updateRequest.getTaskId());

            Task task = taskRepository.findById(taskId).orElseThrow(
                    ()->new NotFoundException("Task not found.")
            );

            if(!task.getPlanId().equals(planId)){
                throw new BusinessException("Task with id " + taskId + " does not belong to plan.");
            }

            if(!task.getAssigneeId().equals(userId)){
                throw new BusinessException("Only user with id " + task.getAssigneeId() + " can update task "+ taskId);
            }

            task.setIsCompleted(updateRequest.getIsCompleted());
            taskRepository.save(task);
        }

        //Update progress
        float progress = calculateProgress(planId);
        planService.updateProgress(planId, progress);
    }

    @Override
    public Set<UUID> updateTasksAssignee(UpdateTasksAssigneeRequest request) {
        UUID userId = UUID.fromString(request.getUserId());
        UUID planId = UUID.fromString(request.getPlanId());

        planService.validateUpdatePlanRequest(planId);

        Set<UUID> assignees = new LinkedHashSet<>();

        for(UpdateTaskAssigneeRequest updateRequest : request.getRequestsList()){
            UUID taskId = UUID.fromString(updateRequest.getTaskId());

            Task task = taskRepository.findById(taskId).orElseThrow(
                    ()->new NotFoundException("Task not found.")
            );

            if(!task.getPlanId().equals(planId)){
                throw new BusinessException("Task with id " + taskId + " does not belong to plan.");
            }

            //If change assignee -> reset task status
            task.setAssigneeId(userId);
            task.setIsCompleted(false);

            taskRepository.save(task);
            assignees.add(userId);
        }

        //Update progress
        float progress = calculateProgress(planId);
        planService.updateProgress(planId, progress);

        return assignees;
    }

    @Override
    public void deleteTasks(DeleteTasksRequest request) {
        UUID planId = UUID.fromString(request.getPlanId());
        planService.validateUpdatePlanRequest(planId);

        int taskCount = taskRepository.countTaskByPlanId(planId);
        if(taskCount <= request.getTaskIdsCount()){
            throw new BusinessException("The plan must have at least one task.");
        }

        for(String idStr : request.getTaskIdsList()){
            UUID taskId = UUID.fromString(idStr);

            Task task = taskRepository.findById(taskId).orElseThrow(
                    ()->new NotFoundException("Task not found.")
            );

            if(!task.getPlanId().equals(planId)){
                throw new BusinessException("Task with id " + taskId + " does not belong to plan.");
            }

            taskRepository.delete(task);
        }

        float progress = calculateProgress(planId);
        planService.updateProgress(planId, progress);
    }

    @Override
    public List<UUID> getAllAssigneeForPlan(UUID planId) {
        return taskRepository.findAllAssigneeIdsByPlanId(planId);
    }

    @Override
    public boolean isAssignedForPlan(UUID userId, UUID planId) {
        return taskRepository.existsByPlanIdAndAssigneeId(planId, userId);
    }

    @Override
    public void deleteAllByPlanId(UUID planId) {
        taskRepository.deleteAllByPlanId(planId);
    }

    private float calculateProgress(UUID planId) {
        int totalTasks = taskRepository.countTaskByPlanId(planId);
        if(totalTasks == 0) return 0;
        int completedTasks = taskRepository.countTaskByPlanIdAndIsCompletedTrue(planId);
        return (float)completedTasks/totalTasks;
    }
}
