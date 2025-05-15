package com.study.apigateway.service.Plan;

import com.study.apigateway.dto.Plan.Plan.request.CreatePersonalPlanRequestDto;
import com.study.apigateway.dto.Plan.Plan.request.CreatePlanRequestDto;
import com.study.apigateway.dto.Plan.Plan.request.CreateTeamPlanRequestDto;
import com.study.apigateway.dto.Plan.Plan.response.PlanResponseDto;
import com.study.apigateway.dto.Plan.Task.request.CreateTaskRequestDto;
import com.study.apigateway.grpc.PlanServiceGrpcClient;
import com.study.apigateway.grpc.TeamServiceGrpcClient;
import com.study.apigateway.mapper.PlanMapper;
import com.study.apigateway.mapper.TaskMapper;
import com.study.planservice.grpc.PlanResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService {
    private final TeamServiceGrpcClient teamServiceGrpc;
    private final PlanServiceGrpcClient planServiceGrpc;

    @Override
    public Mono<PlanResponseDto> createPersonalPlan(UUID userId, CreatePersonalPlanRequestDto request) {
        return Mono.fromCallable(()->{
            //Create plan
            CreatePlanRequestDto dto = CreatePlanRequestDto.builder()
                    .name(request.getName())
                    .description(request.getDescription())
                    .startAt(request.getStartAt())
                    .endAt(request.getEndAt())
                    .build();

            PlanResponse plan = planServiceGrpc.createPlan(userId, null, dto);
            UUID planId = UUID.fromString(plan.getId());

            //Create tasks
            List<CreateTaskRequestDto> tasksDto = new ArrayList<>();
            for(String task : request.getTasks() ) {
                CreateTaskRequestDto taskDto = TaskMapper.toCreateTaskRequestDto(task, userId);
                tasksDto.add(taskDto);
            }
            planServiceGrpc.createTasks(userId, planId, tasksDto);

            //Create reminders
            List<LocalDateTime> remindTimes = request.getRemindTimes();
            if(remindTimes != null && !remindTimes.isEmpty()) {
                planServiceGrpc.createPlanReminders(planId, remindTimes);
            }

            return PlanMapper.toPlanResponseDto(plan);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<PlanResponseDto> createTeamPlan(UUID userId, CreateTeamPlanRequestDto request) {
        return Mono.fromCallable(()->{
            //Create plan
            teamServiceGrpc.validateUpdateTeamResource(userId, request.getTeamId());

            CreatePlanRequestDto dto = CreatePlanRequestDto.builder()
                    .name(request.getName())
                    .description(request.getDescription())
                    .startAt(request.getStartAt())
                    .endAt(request.getEndAt())
                    .build();

            PlanResponse plan = planServiceGrpc.createPlan(userId, request.getTeamId(), dto);
            UUID planId = UUID.fromString(plan.getId());

            //Create tasks
            planServiceGrpc.createTasks(userId, planId, request.getTasks());

            //Create reminders
            List<LocalDateTime> remindTimes = request.getRemindTimes();
            if(remindTimes != null && !remindTimes.isEmpty()) {
                planServiceGrpc.createPlanReminders(planId, remindTimes);
            }

            return PlanMapper.toPlanResponseDto(plan);
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
