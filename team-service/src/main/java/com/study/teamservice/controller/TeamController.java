package com.study.teamservice.controller;

import com.study.teamservice.entity.Team;
import com.study.teamservice.grpc.*;
import com.study.teamservice.mapper.TeamMapper;
import com.study.teamservice.service.TeamService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

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
    public void getFirstTeam(GetFirstTeamRequest request, StreamObserver<TeamResponse> responseObserver){

    }

    @Override
    public void getTeamById(GetTeamByIdRequest request, StreamObserver<TeamResponse> responseObserver){

    }

    @Override
    public void searchTeamByName(SearchTeamByNameRequest request, StreamObserver<SearchTeamByNameResponse> responseObserver){

    }

    @Override
    public void updateTeam(UpdateTeamRequest request, StreamObserver<TeamResponse> responseObserver){

    }

    @Override
    public void removeTeam(RemoveTeamRequest request, StreamObserver<RemoveTeamResponse> responseObserver){

    }
}
