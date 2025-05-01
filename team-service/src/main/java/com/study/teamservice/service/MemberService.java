package com.study.teamservice.service;

import com.study.common.enums.TeamRole;
import com.study.common.exceptions.BusinessException;
import com.study.common.exceptions.NotFoundException;
import com.study.common.mappers.TeamRoleMapper;
import com.study.teamservice.entity.Team;
import com.study.teamservice.entity.TeamUser;
import com.study.teamservice.grpc.*;
import com.study.teamservice.repository.TeamUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final TeamUserRepository teamUserRepository;
    private final TeamService teamService;

    private static final int DEFAULT_SIZE = 10;

    public void createInvitation(CreateInvitationRequest request) {
        UUID teamId = UUID.fromString(request.getTeamId());
        UUID inviterId = UUID.fromString(request.getInviterId());
        UUID inviteeId = UUID.fromString(request.getInviteeId());

        if(!teamService.existsById(teamId)) {
            throw new NotFoundException("Team does not exist");
        }

        if(!teamUserRepository.existsByUserIdAndTeamId(inviterId, teamId)) {
            throw new NotFoundException("User id or team id is incorrect");
        }

        if(teamUserRepository.existsByUserIdAndTeamId(inviteeId, teamId)) {
           throw new BusinessException("The invitee is already in the team");
        }
    }

    public Team joinTeam(JoinTeamRequest request) {
        UUID userId = UUID.fromString(request.getUserId());

        Team team = teamService.getByTeamCode(request.getTeamCode());

        if(team == null) {
            throw new NotFoundException("Team does not exist");
        }

        if(teamUserRepository.existsByUserIdAndTeamId(userId, team.getId())) {
            throw new BusinessException("User is already in the team");
        }

        saveMember(team.getId(), userId, TeamRole.MEMBER);

        return team;
    }

    public List<Team> getUserTeams(GetUserTeamsRequest request) {
        //Validate request
        int size = request.getSize() > 0 ? request.getSize() : DEFAULT_SIZE;
        LocalDate cursor = request.getCursor().isEmpty() ? null : LocalDate.parse(request.getCursor());
        UUID userId = UUID.fromString(request.getUserId());

        Pageable pageable = PageRequest.of(0, size, Sort.by("joinDate").descending());

        //Get result
        List<TeamUser> teamUsers = cursor != null ?
                teamUserRepository.findByUserIdAndJoinDateBeforeOrderByJoinDateDesc(userId, cursor, pageable) :
                teamUserRepository.findByUserIdOrderByJoinDateDesc(userId, pageable);

        //Get teams with unchanged position
        List<UUID> teamIds = teamUsers.stream().map(TeamUser::getTeamId).toList();
        List<Team> teams = new ArrayList<>();

        for(UUID teamId : teamIds) {
            Team team = teamService.getTeamById(teamId);
            teams.add(team);
        }

        return teams;
    }

    public List<Team> searchUserTeamByName(SearchUserTeamByNameRequest request) {
        int size = request.getSize() > 0 ? request.getSize() : DEFAULT_SIZE;
        LocalDate cursor = request.getCursor().isEmpty() ? null : LocalDate.parse(request.getCursor());
        UUID userId = UUID.fromString(request.getUserId());
        String keyword = "%" + request.getKeyword().trim() + "%";

        Pageable pageable = PageRequest.of(0, size, Sort.by("tu.joinDate").descending());

        return cursor != null ?
                teamUserRepository.searchTeamsByUserAndNameWithCursor(userId, keyword, cursor, pageable) :
                teamUserRepository.searchTeamsByUserAndName(userId, keyword, pageable);
    }

    public TeamUser getTeamMember(GetTeamMemberRequest request) {
        UUID userId = UUID.fromString(request.getUserId());
        UUID teamId = UUID.fromString(request.getTeamId());

        TeamUser teamUser = teamUserRepository.findByUserIdAndTeamId(userId, teamId);

        if(teamUser == null) {
            throw new NotFoundException("User id or team id is incorrect");
        }

        return teamUser;
    }

    public List<TeamUser> getTeamMembers(GetTeamMembersRequest request) {
        int size = request.getSize() > 0 ? request.getSize() : DEFAULT_SIZE;
        LocalDate cursor = request.getCursor().isEmpty() ? null : LocalDate.parse(request.getCursor());

        List<TeamRole> roleOrder = Arrays.asList(TeamRole.CREATOR, TeamRole.ADMIN, TeamRole.MEMBER);

        Pageable pageable = PageRequest.of(0, size, Sort.by(
                Sort.Order.asc("role").ignoreCase(),
                Sort.Order.asc("joinDate")
        ));

        if (cursor != null) {
            return teamUserRepository.findByTeamIdAndRoleInAndJoinDateGreaterThan(
                    UUID.fromString(request.getTeamId()),
                    roleOrder,
                    cursor,
                    pageable
            );
        }
        else {
            return teamUserRepository.findByTeamIdAndRoleIn(
                    UUID.fromString(request.getTeamId()),
                    roleOrder,
                    pageable
            );
        }
    }

    public List<TeamUser> getAllTeamMembers(GetAllTeamMembersRequest request){
        UUID teamId = UUID.fromString(request.getTeamId());
        return teamUserRepository.findByTeamId(teamId);
    }

    public void updateTeamMemberRole(UpdateMemberRoleRequest request) {
        UUID teamId = UUID.fromString(request.getTeamId());
        UUID userId = UUID.fromString(request.getUserId());
        UUID memberId = UUID.fromString(request.getMemberId());

        validateUpdateMemberRequest(teamId, userId, memberId);

        TeamUser member = teamUserRepository.findByUserIdAndTeamId(memberId, teamId);

        if(request.getRole() == com.study.teamservice.grpc.TeamRole.CREATOR)
            throw new BusinessException("Member role can't be updated to 'CREATOR'");

        member.setRole(TeamRoleMapper.toEnum(request.getRole()));

        teamUserRepository.save(member);
    }

    public void removeTeamMember(RemoveTeamMemberRequest request) {
        UUID teamId = UUID.fromString(request.getTeamId());
        UUID userId = UUID.fromString(request.getUserId());
        UUID memberId = UUID.fromString(request.getMemberId());

        if(userId == memberId){
            throw new BusinessException("User id and member id are the same.");
        }

        validateUpdateMemberRequest(teamId, userId, memberId);
        teamService.decreaseMember(teamId);
        teamUserRepository.deleteByUserIdAndTeamId(memberId, teamId);
    }

    public void leaveTeam(LeaveTeamRequest request) {
        UUID teamId = UUID.fromString(request.getTeamId());
        UUID userId = UUID.fromString(request.getUserId());

        TeamUser teamUser = teamUserRepository.findByUserIdAndTeamId(userId, teamId);

        if(teamUser == null) {
            throw new NotFoundException("User is not part of the team");
        }

        // Check if any non-member (ADMIN/CREATOR) exists in the team
        long nonMemberCount = teamUserRepository.countNonMemberByTeamId(teamId);
        if (nonMemberCount == 0) {
            throw new BusinessException("You are the last manager of the team." +
                    " Please hand over your responsibilities before leaving.");
        }

        teamService.decreaseMember(teamId);
        teamUserRepository.deleteByUserIdAndTeamId(userId, teamId);
    }


    public void saveMember(UUID teamId, UUID userId, TeamRole role) {
        teamService.increaseMember(teamId);

        TeamUser teamUser = TeamUser.builder()
                .teamId(teamId)
                .userId(userId)
                .role(role)
                .joinDate(LocalDate.now())
                .build();

        teamUserRepository.save(teamUser);
    }

    public boolean isTeamManagedByUser(UUID teamId, UUID userId) {
        TeamUser teamUser = teamUserRepository.findByUserIdAndTeamId(userId, teamId);
        if(teamUser == null) return false;
        return teamUser.getRole() != TeamRole.MEMBER;
    }

    public long countUserTeams(UUID userId){
        return teamUserRepository.countByUserId(userId);
    }

    public long countUserTeamsByKeyword(UUID userId, String keyword){
        return teamUserRepository.countUserTeamsByKeyword(userId, keyword);
    }

    public long countMembers(UUID teamId){
        return teamUserRepository.countByTeamId(teamId);
    }

    public String calculateNextPageCursor(UUID userId, List<Team> teams, int requestSize) {
        if(teams.isEmpty()) return "";

        Team lastTeam = teams.get(teams.size() - 1);
        TeamUser memberInfo = teamUserRepository.findByUserIdAndTeamId(userId, lastTeam.getId());

        return teams.size() == requestSize ? memberInfo.getJoinDate().toString() : "";
    }

    public void validateUpdateTeamPermission(UUID userId, UUID teamId) {
        TeamUser teamUser = teamUserRepository.findByUserIdAndTeamId(userId, teamId);

        if(teamUser == null)
            throw new NotFoundException("User is not part of this team");

        if(teamUser.getRole() == TeamRole.MEMBER)
            throw new BusinessException("User doesn't have permission to update this team");
    }

    private void validateUpdateMemberRequest(UUID teamId, UUID userId, UUID memberId) {
        validateUpdateTeamPermission(userId, teamId);

        TeamUser member = teamUserRepository.findByUserIdAndTeamId(memberId, teamId);

        if(member == null) {
            throw new NotFoundException("Member id or team id is incorrect");
        }

        if(member.getRole() == TeamRole.CREATOR) {
            throw new BusinessException("Can't update the creator of the team");
        }
    }

    public void deleteAllMembers(UUID teamId) {
        teamUserRepository.deleteAllByTeamId(teamId);
    }
}
