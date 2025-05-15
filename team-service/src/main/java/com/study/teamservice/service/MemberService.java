package com.study.teamservice.service;

import com.study.common.enums.TeamRole;
import com.study.teamservice.entity.Team;
import com.study.teamservice.entity.TeamUser;
import com.study.teamservice.grpc.*;

import java.util.List;
import java.util.UUID;

public interface MemberService {
    void createInvitation(CreateInvitationRequest request);
    Team joinTeam(JoinTeamRequest request);
    List<TeamUser> getUserTeams(GetUserTeamsRequest request);
    long countUserTeams(UUID teamId);
    List<TeamUser> searchUserTeamsByName(SearchUserTeamByNameRequest request);
    long countUserTeamsByKeyword(UUID userId, String keyword);
    String calculateTeamNextPageCursor(UUID userId, List<Team> teams, int requestSize);
    List<TeamUser> getTeamMembers(GetTeamMembersRequest request);
    long countTeamMembers(UUID teamId);
    String calculateMemberNextPageCursor(List<TeamUser> teamUsers, int requestSize);
    List<TeamUser> getAllTeamMembers(GetAllTeamMembersRequest request);
    TeamUser getTeamMember(GetTeamMemberRequest request);
    void updateTeamMemberRole(UpdateMemberRoleRequest request);
    void removeTeamMember(RemoveTeamMemberRequest request);
    void leaveTeam(LeaveTeamRequest request);
    void validateUpdateTeamResource(ValidateUpdateTeamResourceRequest request);
    void validateUsersInTeam(ValidateUsersInTeamRequest request);

    boolean isTeamManagedByUser(UUID teamId, UUID userId);
    void validateUpdateTeamPermission(UUID userId, UUID teamId);
    void saveMember(UUID teamId, UUID userId, TeamRole role);
    List<UUID> getTeamMemberIds(UUID teamId);
    void deleteAllMembers(UUID teamId);
}
