package com.study.apigateway.service.Team;

import com.study.apigateway.dto.Team.request.CreateTeamRequestDto;
import com.study.apigateway.dto.Team.request.UpdateTeamRequestDto;
import com.study.apigateway.dto.Action.ActionResponseDto;
import com.study.apigateway.dto.Team.response.ListTeamResponseDto;
import com.study.apigateway.dto.Team.response.TeamResponseDto;
import com.study.apigateway.grpc.TeamServiceGrpcClient;
import com.study.apigateway.grpc.UserServiceGrpcClient;
import com.study.apigateway.mapper.ActionMapper;
import com.study.apigateway.mapper.TeamMapper;
import com.study.common.exceptions.NotFoundException;
import com.study.common.grpc.ActionResponse;
import com.study.teamservice.grpc.ListTeamResponse;
import com.study.teamservice.grpc.TeamResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {
    private final TeamServiceGrpcClient grpcClient;
    private final UserServiceGrpcClient userClient;

    @Override
    public Mono<TeamResponseDto> createTeam(UUID userId, CreateTeamRequestDto request) {
        return Mono.fromCallable(() -> {
            if(!userClient.existsById(userId).getExists()){
                throw new NotFoundException("User not found");
            }
            TeamResponse team = grpcClient.createTeam(userId, request);
            return TeamMapper.toResponseDto(team);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<TeamResponseDto> getTeamById(UUID id) {
        return Mono.fromCallable(()-> {
            TeamResponse team = grpcClient.getTeamById(id);
            return TeamMapper.toResponseDto(team);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<ListTeamResponseDto> getUserTeams(UUID userId, LocalDate cursor, int size) {
        return Mono.fromCallable(() -> {
            ListTeamResponse response = grpcClient.getUserTeams(userId, cursor, size);

            List<TeamResponseDto> teams = TeamMapper.toResponseDtoList(response.getTeamsList());
            LocalDate nextCursor = response.getNextCursor().isEmpty() ? null : LocalDate.parse(response.getNextCursor());

            return ListTeamResponseDto.builder()
                    .teams(teams)
                    .total(response.getTotal())
                    .nextCursor(nextCursor)
                    .build();
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<ListTeamResponseDto> searchUserTeamByName(UUID userId, String keyword, LocalDate cursor, int size) {
        return Mono.fromCallable(() -> {
            ListTeamResponse response = grpcClient.searchUserTeamByName(userId, keyword, cursor, size);

            List<TeamResponseDto> teams = TeamMapper.toResponseDtoList(response.getTeamsList());
            LocalDate nextCursor = response.getNextCursor().isEmpty() ? null : LocalDate.parse(response.getNextCursor());

            return ListTeamResponseDto.builder()
                    .teams(teams)
                    .total(response.getTotal())
                    .nextCursor(nextCursor)
                    .build();

        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<TeamResponseDto> updateTeam(UUID userId, UUID teamId, UpdateTeamRequestDto request) {
        return Mono.fromCallable(()->{
            TeamResponse team = grpcClient.updateTeam(userId, teamId, request);
            return TeamMapper.toResponseDto(team);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<ActionResponseDto> deleteTeam(UUID id, UUID userId) {
        return Mono.fromCallable(()->{
            ActionResponse response = grpcClient.deleteTeam(id, userId);
            return ActionMapper.toResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
