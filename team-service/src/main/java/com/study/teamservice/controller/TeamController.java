package com.study.teamservice.controller;

import com.study.common.enums.TeamRole;
import com.study.common.grpc.ActionResponse;
import com.study.teamservice.entity.Team;
import com.study.teamservice.entity.TeamUser;
import com.study.teamservice.grpc.*;
import com.study.teamservice.mapper.TeamMapper;
import com.study.teamservice.mapper.TeamUserMapper;
import com.study.teamservice.service.MemberService;
import com.study.teamservice.service.TeamNotificationService;
import com.study.teamservice.service.TeamService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.*;

@GrpcService
@RequiredArgsConstructor
public class TeamController extends TeamServiceGrpc.TeamServiceImplBase {
    private final TeamService teamService;
    private final MemberService memberService;
    private final TeamNotificationService teamNotificationService;

    //Team section
    @Override
    public void createTeam(CreateTeamRequest request, StreamObserver<TeamResponse> responseObserver){
        //Create team
        Team team = teamService.createTeam(request);

        //Save creator info
        UUID creatorId = UUID.fromString(request.getCreatorId());
        memberService.saveMember(team.getId(), creatorId, TeamRole.CREATOR);

        TeamResponse response = TeamMapper.toTeamResponse(team);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getTeamById(GetTeamByIdRequest request, StreamObserver<TeamDetailResponse> responseObserver){
        UUID id = UUID.fromString(request.getId());
        Team team = teamService.getTeamById(id);
        TeamDetailResponse response = TeamMapper.toTeamDetailResponse(team);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateTeam(UpdateTeamRequest request, StreamObserver<TeamResponse> responseObserver){
        UUID userId = UUID.fromString(request.getUserId());
        UUID teamId = UUID.fromString(request.getId());

        memberService.validateUpdateTeamPermission(userId, teamId);

        Set<String> updatedFields = teamService.updateTeam(request);
        teamNotificationService.publishTeamUpdateNotification(userId, teamId, updatedFields);

        Team team = teamService.getTeamById(teamId);
        TeamResponse response = TeamMapper.toTeamResponse(team);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void uploadTeamAvatar(UploadTeamAvatarRequest request, StreamObserver<ActionResponse> responseObserver){
        UUID userId = UUID.fromString(request.getUserId());
        UUID teamId = UUID.fromString(request.getTeamId());
        Set<String> updatedFields = Set.of("avatar");

        memberService.validateUpdateTeamPermission(userId, teamId);
        teamService.uploadTeamAvatar(request);
        teamNotificationService.publishTeamUpdateNotification(userId, teamId, updatedFields);

        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Upload avatar success")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void deleteTeam(DeleteTeamRequest request, StreamObserver<ActionResponse> responseObserver){
        UUID userId = UUID.fromString(request.getUserId());
        UUID teamId = UUID.fromString(request.getId());

        memberService.validateUpdateTeamPermission(userId, teamId);
        teamService.deleteTeam(teamId);
        memberService.deleteAllMembers(teamId);
        teamNotificationService.publishTeamDeletionNotification(userId, teamId);

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
        UUID inviterId = UUID.fromString(request.getInviterId());
        UUID inviteeId = UUID.fromString(request.getInviteeId());
        UUID teamId = UUID.fromString(request.getTeamId());

        memberService.createInvitation(request);
        teamNotificationService.sentInvitation(inviterId, inviteeId, teamId);

        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Create invitation success")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void joinTeam(JoinTeamRequest request, StreamObserver<ActionResponse> responseObserver){
        UUID userId = UUID.fromString(request.getUserId());

        Team team = memberService.joinTeam(request);
        teamNotificationService.publishUserJoinedTeamNotification(userId, team.getId());

        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Join team success")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getUserTeams(GetUserTeamsRequest request, StreamObserver<ListTeamResponse> responseObserver){
        List<Team> teams = memberService.getUserTeams(request);

        UUID userId = UUID.fromString(request.getUserId());

        List<TeamSummaryResponse> teamResponses = toListTeamSummaryResponse(userId, teams);
        long total = memberService.countUserTeams(userId);
        String nextCursor = memberService.calculateNextPageCursor(userId, teams, request.getSize());

        ListTeamResponse response = TeamMapper.toListTeamResponse(teamResponses, total, nextCursor);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void searchUserTeamByName(SearchUserTeamByNameRequest request, StreamObserver<ListTeamResponse> responseObserver){
        List<Team> teams = memberService.searchUserTeamByName(request);

        UUID userId = UUID.fromString(request.getUserId());

        List<TeamSummaryResponse> teamResponses = toListTeamSummaryResponse(userId, teams);

        String nextCursor = memberService.calculateNextPageCursor(userId, teams, request.getSize());
        long total = memberService.countUserTeamsByKeyword(userId, request.getKeyword());

        ListTeamResponse response =  TeamMapper.toListTeamResponse(teamResponses, total, nextCursor);

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
    public void getAllTeamMembers(GetAllTeamMembersRequest request, StreamObserver<AllTeamMembersResponse> responseObserver){
        List<TeamUser> members = memberService.getAllTeamMembers(request);
        AllTeamMembersResponse response = TeamUserMapper.toAllTeamMembersResponse(members);
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
        UUID userId = UUID.fromString(request.getUserId());
        UUID teamId = UUID.fromString(request.getTeamId());

        memberService.removeTeamMember(request);
        if(memberService.countMembers(teamId) > 1) {
            teamNotificationService.publishUserLeftTeamNotification(userId, teamId);
        }

        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Remove team member success")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void leaveTeam(LeaveTeamRequest request, StreamObserver<ActionResponse> responseObserver){
        UUID userId = UUID.fromString(request.getUserId());
        UUID teamId = UUID.fromString(request.getTeamId());

        memberService.leaveTeam(request);
        if(memberService.countMembers(teamId) > 0)
            teamNotificationService.publishUserLeftTeamNotification(userId, teamId);
        else
            teamNotificationService.publishTeamDeletionNotification(userId, teamId);

        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Leave team success")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }


    private List<TeamSummaryResponse> toListTeamSummaryResponse(UUID userId, List<Team> teams) {
        List<TeamSummaryResponse> teamResponses = new ArrayList<>();

        for (Team team : teams) {
            boolean managedByUser = memberService.isTeamManagedByUser(team.getId(), userId);
            TeamSummaryResponse teamSummaryResponse = TeamMapper.toTeamSummaryResponse(team, managedByUser);
            teamResponses.add(teamSummaryResponse);
        }

        return teamResponses;
    }
}
