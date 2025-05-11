package com.study.teamservice.service.impl;

import com.study.common.enums.TeamRole;
import com.study.common.exceptions.BusinessException;
import com.study.common.exceptions.NotFoundException;
import com.study.common.mappers.TeamRoleMapper;
import com.study.teamservice.entity.Team;
import com.study.teamservice.entity.TeamUser;
import com.study.teamservice.grpc.*;
import com.study.teamservice.repository.TeamUserRepository;
import com.study.teamservice.service.MemberService;
import com.study.teamservice.service.TeamService;
import com.study.common.utils.CursorUtil;
import com.study.common.utils.DecodedCursor;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final TeamUserRepository teamUserRepository;
    private final TeamService teamService;

    private static final int DEFAULT_SIZE = 10;

    @Override
    public void createInvitation(CreateInvitationRequest request) {
        UUID teamId = UUID.fromString(request.getTeamId());
        UUID inviterId = UUID.fromString(request.getInviterId());
        UUID inviteeId = UUID.fromString(request.getInviteeId());

        validateUpdateMemberPermission(teamId, inviterId);

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

    @Override
    public Team joinTeam(JoinTeamRequest request) {
        UUID userId = UUID.fromString(request.getUserId());

        Team team = teamService.getTeamByTeamCode(request.getTeamCode());

        if(team == null) {
            throw new NotFoundException("Team does not exist");
        }

        if(teamUserRepository.existsByUserIdAndTeamId(userId, team.getId())) {
            throw new BusinessException("User is already in the team");
        }

        saveMember(team.getId(), userId, TeamRole.MEMBER);

        return team;
    }

    @Override
    public List<TeamUser> getUserTeams(GetUserTeamsRequest request) {
        //Validate request
        int size = request.getSize() > 0 ? request.getSize() : DEFAULT_SIZE;
        String cursor = request.getCursor();
        UUID userId = UUID.fromString(request.getUserId());

        Pageable pageable = PageRequest.of(0, size, Sort.by("joinDate").descending()
                .and(Sort.by("id").ascending()));

        if (!cursor.isEmpty()) {
            DecodedCursor decodedCursor = CursorUtil.decodeCursor(cursor);
            return teamUserRepository.findByUserIdWithCursor(
                    userId, decodedCursor.getDate(), decodedCursor.getId(), pageable
            );
        }

        return teamUserRepository.findByUserIdOrderByJoinDateDescIdAsc(userId, pageable);
    }

    @Override
    public List<TeamUser> searchUserTeamsByName(SearchUserTeamByNameRequest request) {
        int size = request.getSize() > 0 ? request.getSize() : DEFAULT_SIZE;
        String cursor = request.getCursor();
        String keyword = "%" + request.getKeyword().trim() + "%";
        UUID userId = UUID.fromString(request.getUserId());

        Pageable pageable = PageRequest.of(0, size, Sort.by("tu.joinDate").descending().and(Sort.by("tu.id").ascending()));

        if (!cursor.isEmpty()) {
            DecodedCursor decodedCursor = CursorUtil.decodeCursor(cursor);
            teamUserRepository.searchTeamsByUserAndNameWithCursor(
                    userId, keyword, decodedCursor.getDate(), decodedCursor.getId(), pageable
            );
        }

        return teamUserRepository.searchTeamsByUserAndName(userId, keyword, pageable);
    }

    @Override
    public TeamUser getTeamMember(GetTeamMemberRequest request) {
        UUID userId = UUID.fromString(request.getUserId());
        UUID teamId = UUID.fromString(request.getTeamId());

        TeamUser teamUser = teamUserRepository.findByUserIdAndTeamId(userId, teamId);

        if(teamUser == null) {
            throw new NotFoundException("User id or team id is incorrect");
        }

        return teamUser;
    }

    @Override
    public List<TeamUser> getTeamMembers(GetTeamMembersRequest request) {
        int size = request.getSize() > 0 ? request.getSize() : DEFAULT_SIZE;
        String cursor = request.getCursor();

        List<TeamRole> roleOrder = Arrays.asList(TeamRole.CREATOR, TeamRole.ADMIN, TeamRole.MEMBER);
        Pageable pageable = PageRequest.of(0, size, Sort.by(
                Sort.Order.asc("role").ignoreCase(),
                Sort.Order.asc("joinDate"),
                Sort.Order.asc("id")
        ));

        if (!cursor.isEmpty()) {
            DecodedCursor decodedCursor = CursorUtil.decodeCursor(cursor);
            return teamUserRepository.findByTeamIdAndRoleInAndJoinDateGreaterThanAndIdGreaterThan(
                    UUID.fromString(request.getTeamId()),
                    roleOrder,
                    decodedCursor.getDate(),
                    decodedCursor.getId(),
                    pageable
            );
        }

        return teamUserRepository.findByTeamIdAndRoleIn(
                UUID.fromString(request.getTeamId()),
                roleOrder,
                pageable
        );
    }

    @Override
    public List<TeamUser> getAllTeamMembers(GetAllTeamMembersRequest request){
        UUID teamId = UUID.fromString(request.getTeamId());
        return teamUserRepository.findByTeamId(teamId);
    }

    @Override
    public void updateTeamMemberRole(UpdateMemberRoleRequest request) {
        UUID teamId = UUID.fromString(request.getTeamId());
        UUID userId = UUID.fromString(request.getUserId());
        UUID memberId = UUID.fromString(request.getMemberId());

        TeamUser user = teamUserRepository.findByUserIdAndTeamId(userId, teamId);

        if(user == null) {
            throw new NotFoundException("User id or team id is incorrect");
        }

        if(user.getRole() != TeamRole.CREATOR){
            throw new BusinessException("Only the creator can change a member's role");
        }

        TeamUser member = teamUserRepository.findByUserIdAndTeamId(memberId, teamId);

        if(member == null) {
            throw new NotFoundException("Member id or team id is incorrect");
        }

        if(request.getRole() == com.study.teamservice.grpc.TeamRole.CREATOR){
            user.setRole(TeamRole.ADMIN);
            teamUserRepository.save(user);
        }

        member.setRole(TeamRoleMapper.toEnum(request.getRole()));
        teamUserRepository.save(member);
    }

    @Transactional
    @Override
    public void removeTeamMember(RemoveTeamMemberRequest request) {
        UUID teamId = UUID.fromString(request.getTeamId());
        UUID userId = UUID.fromString(request.getUserId());
        UUID memberId = UUID.fromString(request.getMemberId());

        if(userId == memberId){
            throw new BusinessException("User id and member id are the same.");
        }

        if(!teamUserRepository.existsByUserIdAndTeamId(memberId, teamId)){
            throw new NotFoundException("Member id or team id is incorrect.");
        }

        validateUpdateMemberPermission(teamId, userId);

        teamService.decreaseMember(teamId);
        teamUserRepository.deleteByUserIdAndTeamId(memberId, teamId);
    }

    @Transactional
    @Override
    public void leaveTeam(LeaveTeamRequest request) {
        UUID teamId = UUID.fromString(request.getTeamId());
        UUID userId = UUID.fromString(request.getUserId());

        TeamUser teamUser = teamUserRepository.findByUserIdAndTeamId(userId, teamId);

        if(teamUser == null) {
            throw new NotFoundException("User is not part of the team");
        }

        if (teamUser.getRole() == TeamRole.CREATOR) {
            throw new BusinessException("You are the creator of the team." +
                    " Please hand over your responsibilities before leaving.");
        }

        teamService.decreaseMember(teamId);
        teamUserRepository.deleteByUserIdAndTeamId(userId, teamId);
    }


    @Override
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

    @Override
    public boolean isTeamManagedByUser(UUID teamId, UUID userId) {
        TeamUser teamUser = teamUserRepository.findByUserIdAndTeamId(userId, teamId);
        if(teamUser == null) return false;
        return teamUser.getRole() != TeamRole.MEMBER;
    }

    @Override
    public long countUserTeams(UUID userId){
        return teamUserRepository.countByUserId(userId);
    }

    @Override
    public long countUserTeamsByKeyword(UUID userId, String keyword){
        return teamUserRepository.countUserTeamsByKeyword(userId, keyword);
    }

    @Override
    public long countTeamMembers(UUID teamId){
        return teamUserRepository.countByTeamId(teamId);
    }

    @Override
    public String calculateMemberNextPageCursor(List<TeamUser> teamUsers, int requestSize) {
        if(teamUsers.isEmpty()) return "";
        TeamUser lastMember = teamUsers.get(teamUsers.size() - 1);
        return teamUsers.size() == requestSize ? CursorUtil.encodeCursor(lastMember.getJoinDate(), lastMember.getId()) : "";
    }

    @Override
    public String calculateTeamNextPageCursor(UUID userId, List<Team> teams, int requestSize) {
        if(teams.isEmpty()) return "";

        Team lastTeam = teams.get(teams.size() - 1);
        TeamUser memberInfo = teamUserRepository.findByUserIdAndTeamId(userId, lastTeam.getId());

        return teams.size() == requestSize ? CursorUtil.encodeCursor(memberInfo.getJoinDate(), memberInfo.getId()) : "";
    }

    @Override
    public void validateUpdateTeamPermission(UUID userId, UUID teamId) {
        TeamUser teamUser = teamUserRepository.findByUserIdAndTeamId(userId, teamId);

        if(teamUser == null)
            throw new NotFoundException("User is not part of this team");

        if(teamUser.getRole() != TeamRole.CREATOR)
            throw new BusinessException("Only the creator has permission to update this team");
    }

    private void validateUpdateMemberPermission(UUID teamId, UUID userId) {
        TeamUser user = teamUserRepository.findByUserIdAndTeamId(userId, teamId);

        if(user == null){
            throw new NotFoundException("User is not part of this team");
        }

        if(user.getRole() == TeamRole.MEMBER){
            throw new BusinessException("User doesn't have permission to update this team.");
        }
    }

    @Transactional
    @Override
    public void deleteAllMembers(UUID teamId) {
        teamUserRepository.deleteAllByTeamId(teamId);
    }
}
