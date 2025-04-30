package com.study.teamservice.controller;

import com.study.common.grpc.ActionResponse;
import com.study.teamservice.entity.Team;
import com.study.teamservice.entity.TeamUser;
import com.study.teamservice.grpc.*;
import com.study.teamservice.mapper.TeamMapper;
import com.study.teamservice.mapper.TeamUserMapper;
import com.study.teamservice.service.MemberService;
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
    private final MemberService memberService;

    //Team section
    @Override
    public void createTeam(CreateTeamRequest request, StreamObserver<TeamResponse> responseObserver){
        Team team = teamService.createTeam(request);
        TeamResponse response = TeamMapper.toTeamResponse(team);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getTeamById(GetTeamByIdRequest request, StreamObserver<TeamDetailResponse> responseObserver){
        Team team = teamService.getTeamById(request);
        TeamDetailResponse response = TeamMapper.toTeamDetailResponse(team);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getUserTeams(GetUserTeamsRequest request, StreamObserver<ListTeamResponse> responseObserver){
        List<Team> teams = teamService.getUserTeams(request);

        UUID userId = UUID.fromString(request.getUserId());

        List<TeamSummaryResponse> teamResponses = teamService.toListTeamSummaryResponse(teams, userId);
        String nextCursor = memberService.calculateNextPageCursor(userId, teams, request.getSize());
        long total = memberService.countTeams(userId);

        ListTeamResponse response = TeamMapper.toListTeamResponse(teamResponses, total, nextCursor);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void searchUserTeamByName(SearchUserTeamByNameRequest request, StreamObserver<ListTeamResponse> responseObserver){
        List<Team> teams = teamService.searchUserTeamByName(request);

        UUID userId = UUID.fromString(request.getUserId());

        List<TeamSummaryResponse> teamResponses = teamService.toListTeamSummaryResponse(teams, userId);

        String nextCursor = memberService.calculateNextPageCursor(userId, teams, request.getSize());
        long total = teamService.countUserTeamByKeyword(userId, request.getKeyword());

        ListTeamResponse response =  TeamMapper.toListTeamResponse(teamResponses, total, nextCursor);

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
    public void uploadTeamAvatar(UploadTeamAvatarRequest request, StreamObserver<ActionResponse> responseObserver){
        teamService.uploadTeamAvatar(request);
        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Upload avatar success")
                .build();

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


    //Member section
    @Override
    public void createInvitation(CreateInvitationRequest request, StreamObserver<ActionResponse> responseObserver){
        memberService.createInvitation(request);
        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Create invitation success")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void joinTeam(JoinTeamRequest request, StreamObserver<ActionResponse> responseObserver){
        memberService.joinTeam(request);
        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Join team success")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getTeamMember(GetTeamMemberRequest request, StreamObserver<TeamMemberResponse> responseObserver){
        TeamUser teamUser = memberService.getTeamMember(request);
        TeamMemberResponse response = TeamUserMapper.toTeamMemberResponse(teamUser);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getTeamMembers(GetTeamMembersRequest request, StreamObserver<ListTeamMembersResponse> responseObserver){
        List<TeamUser> members = memberService.getTeamMembers(request);
        long total = memberService.countMembers(UUID.fromString(request.getTeamId()));

        ListTeamMembersResponse response = TeamUserMapper
                .toListTeamMemberResponse(members, total, request.getSize());

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateTeamMemberRole(UpdateMemberRoleRequest request, StreamObserver<ActionResponse> responseObserver){
        memberService.updateTeamMemberRole(request);
        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Update member's role success")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void removeTeamMember(RemoveTeamMemberRequest request, StreamObserver<ActionResponse> responseObserver){
        memberService.removeTeamMember(request);
        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Remove team member success")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void leaveTeam(LeaveTeamRequest request, StreamObserver<ActionResponse> responseObserver){
        memberService.leaveTeam(request);
        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Leave team success")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
