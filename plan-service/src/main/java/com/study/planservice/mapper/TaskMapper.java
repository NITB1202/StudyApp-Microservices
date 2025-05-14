package com.study.planservice.mapper;

import com.study.planservice.entity.Task;
import com.study.planservice.grpc.TaskResponse;
import com.study.planservice.grpc.TasksResponse;

import java.util.List;

public class TaskMapper {
    private TaskMapper() {}

    public static TaskResponse toTaskResponse(Task task) {
        return TaskResponse.newBuilder()
                .setId(task.getId().toString())
                .setName(task.getName())
                .setAssigneeId(task.getAssigneeId().toString())
                .setIsCompleted(task.getIsCompleted())
                .build();
    }

    public static TasksResponse toTasksResponse(List<Task> tasks) {
        List<TaskResponse> responses = tasks.stream()
                .map(TaskMapper::toTaskResponse)
                .toList();

        return TasksResponse.newBuilder()
                .addAllTasks(responses)
                .build();
    }
}
