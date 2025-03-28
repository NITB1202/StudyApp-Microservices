package com.study.teamservice.controller;

import com.study.teamservice.entity.Team;
import com.study.teamservice.grpc.*;
import com.study.teamservice.mapper.TeamMapper;
import com.study.teamservice.service.TeamService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;
import java.util.UUID;

@GrpcService
@RequiredArgsConstructor
public class TeamController extends TeamServiceGrpc.TeamServiceImplBase {
    private final TeamService teamService;

    @Override
    public void createTeam(CreateTeamRequest request, StreamObserver<TeamResponse> responseObserver){
        Team team = teamService.createTeam(request);
        TeamResponse response = TeamMapper.toTeamResponse(team);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getFirstTeamId(GetFirstTeamIdRequest request, StreamObserver<GetFirstTeamIdResponse> responseObserver){
        UUID teamId = teamService.getFirstTeamId(request);
        GetFirstTeamIdResponse response = GetFirstTeamIdResponse.newBuilder()
                .setTeamId(teamId.toString())
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getTeamById(GetTeamByIdRequest request, StreamObserver<TeamResponse> responseObserver){
        Team team = teamService.getTeamById(request);
        TeamResponse response = TeamMapper.toTeamResponse(team);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getUserTeams(GetUserTeamsRequest request, StreamObserver<ListTeamResponse> responseObserver){
        List<Team> teams = teamService.getUserTeams(request);
        List<TeamResponse> teamResponses = TeamMapper.toTeamResponseList(teams);

        String nextCursor = !teams.isEmpty() && teams.size() == request.getSize() ? teams.get(teams.size() - 1).getId().toString() : "";
        long total = teamService.countUserTeam(UUID.fromString(request.getUserId()));

        ListTeamResponse response =  ListTeamResponse.newBuilder()
                .addAllTeams(teamResponses)
                .setTotal(total)
                .setNextCursor(nextCursor)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void searchUserTeamByName(SearchUserTeamByNameRequest request, StreamObserver<ListTeamResponse> responseObserver){
        List<Team> teams = teamService.searchUserTeamByName(request);
        List<TeamResponse> teamResponses = TeamMapper.toTeamResponseList(teams);

        String nextCursor = !teams.isEmpty() && teams.size() == request.getSize() ? teams.get(teams.size() - 1).getId().toString() : "";
        long total = teamService.countUserTeamByKeyword(UUID.fromString(request.getUserId()), request.getKeyword());

        ListTeamResponse response =  ListTeamResponse.newBuilder()
                .addAllTeams(teamResponses)
                .setTotal(total)
                .setNextCursor(nextCursor)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateTeam(UpdateTeamRequest request, StreamObserver<TeamResponse> responseObserver){
        Team team = teamService.updateTeam(request);
        TeamResponse response = TeamMapper.toTeamResponse(team);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void deleteTeam(DeleteTeamRequest request, StreamObserver<ActionResponse> responseObserver){
        teamService.deleteTeam(request);
        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Delete team success")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
