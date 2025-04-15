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

        UUID userId = UUID.fromString(request.getUserId());
        String nextCursor = !teams.isEmpty() && teams.size() == request.getSize() ?
                teamService.getJoinDateString(teams.get(teams.size() - 1).getId(), userId) : "";
        long total = teamService.countUserTeam(userId);

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

        UUID userId = UUID.fromString(request.getUserId());
        String nextCursor = !teams.isEmpty() && teams.size() == request.getSize() ?
                teamService.getJoinDateString(teams.get(teams.size() - 1).getId(), userId) : "";
        long total = teamService.countUserTeamByKeyword(userId, request.getKeyword());

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
    public void getTeamMembers(GetTeamMembersRequest request, StreamObserver<GetTeamMembersResponse> responseObserver){
        List<TeamUser> members = memberService.getTeamMembers(request);
        List<TeamMemberResponse> memberResponses = members.stream()
                .map(TeamUserMapper::toTeamMemberResponse)
                .toList();

        long total = memberService.countMembers(UUID.fromString(request.getTeamId()));
        String nextCursor = !memberResponses.isEmpty() && memberResponses.size() == request.getSize() ?
                memberResponses.get(memberResponses.size() - 1).getJoinDate() : "";

        GetTeamMembersResponse response = GetTeamMembersResponse.newBuilder()
                .addAllMembers(memberResponses)
                .setTotal(total)
                .setNextCursor(nextCursor)
                .build();

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
