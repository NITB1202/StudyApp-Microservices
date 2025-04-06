package com.study.teamservice.service;

import com.study.common.enums.TeamRole;
import com.study.common.events.Team.UserLeftTeamEvent;
import com.study.common.exceptions.BusinessException;
import com.study.common.exceptions.NotFoundException;
import com.study.common.mappers.TeamRoleMapper;
import com.study.teamservice.entity.Team;
import com.study.teamservice.entity.TeamUser;
import com.study.common.events.Notification.InvitationCreatedEvent;
import com.study.common.events.Notification.InvitationAcceptEvent;
import com.study.teamservice.event.TeamEventPublisher;
import com.study.common.events.Team.UserJoinedTeamEvent;
import com.study.teamservice.grpc.*;
import com.study.teamservice.repository.TeamRepository;
import com.study.teamservice.repository.TeamUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final TeamUserRepository teamUserRepository;
    private final TeamRepository teamRepository;
    private final TeamEventPublisher teamEventPublisher;

    private static final int DEFAULT_SIZE = 10;
    private static final String INVITATION_CREATED_TOPIC = "invitation-created";
    private static final String USER_JOINED_TOPIC = "user-joined";
    private static final String USER_LEFT_TOPIC = "user-left";

    public void createInvitation(CreateInvitationRequest request) {
        UUID teamId = UUID.fromString(request.getTeamId());
        UUID inviterId = UUID.fromString(request.getInviterId());
        UUID inviteeId = UUID.fromString(request.getInviteeId());

        if(!teamRepository.existsById(teamId)) {
            throw new NotFoundException("Team does not exist");
        }

        if(!teamUserRepository.existsByUserIdAndTeamId(inviterId, teamId)) {
            throw new NotFoundException("User is not in this team");
        }

        if(teamUserRepository.existsByUserIdAndTeamId(inviteeId, teamId)) {
           throw new BusinessException("This user is already in the team");
        }

        InvitationCreatedEvent event = InvitationCreatedEvent.builder()
                .teamId(teamId)
                .fromId(inviterId)
                .toId(inviteeId)
                .build();

        teamEventPublisher.publishEvent(INVITATION_CREATED_TOPIC, event);
    }

    public void joinTeam(JoinTeamRequest request) {
        Team team = teamRepository.findByTeamCode(request.getTeamCode());

        if(team == null) {
            throw new NotFoundException("Team does not exist");
        }

        addNewMember(team.getId(), UUID.fromString(request.getUserId()));
    }

    public TeamUser getTeamMemberById(GetTeamMemberByIdRequest request) {
        TeamUser member = teamUserRepository.findByUserIdAndTeamId(
                UUID.fromString(request.getUserId()), UUID.fromString(request.getTeamId()));

        if(member == null) {
            throw new NotFoundException("This user is not part of the team");
        }
        return member;
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

    public long countMembers(UUID teamId){
        return teamUserRepository.countByTeamId(teamId);
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
        removeMember(teamId, memberId);
    }

    public void leaveTeam(LeaveTeamRequest request) {
        UUID teamId = UUID.fromString(request.getTeamId());
        UUID userId = UUID.fromString(request.getUserId());

        TeamUser teamUser = teamUserRepository.findByUserIdAndTeamId(userId, teamId);

        if(teamUser == null) {
            throw new NotFoundException("User is not part of the team");
        }

        removeMember(teamId, userId);
    }

    @EventListener
    public void acceptInvitation(InvitationAcceptEvent request) {
        addNewMember(request.getTeamId(), request.getUserId());
    }

    public List<UUID> getTeamMembersId(UUID teamId) {
        return teamUserRepository.findByTeamId(teamId).stream()
                .map(TeamUser::getUserId)
                .toList();
    }

    public boolean userDoesNotHavePermissionToUpdate(UUID userId, UUID teamId) {
        TeamUser teamUser = teamUserRepository.findByUserIdAndTeamId(userId, teamId);

        if(teamUser == null)
            throw new NotFoundException("User is not part of this team");

        return teamUser.getRole() == TeamRole.MEMBER;
    }

    public void validateUpdateMemberRequest(UUID teamId, UUID userId, UUID memberId) {
        if(userDoesNotHavePermissionToUpdate(userId, teamId)) {
            throw new BusinessException("User does not have permission to perform this action.");
        }

        TeamUser member = teamUserRepository.findByUserIdAndTeamId(memberId, teamId);

        if(member == null) {
            throw new NotFoundException("This user is not part of the team");
        }

        if(member.getRole() == TeamRole.CREATOR) {
            throw new BusinessException("Can't update the creator of the team");
        }
    }

    private void addNewMember(UUID teamId, UUID userId) {
        Team team = teamRepository.findById(teamId).orElseThrow(
                ()-> new NotFoundException("Team does not exist")
        );

        team.setTotalMembers(team.getTotalMembers() + 1);
        teamRepository.save(team);

        TeamUser teamUser = TeamUser.builder()
                .teamId(teamId)
                .userId(userId)
                .role(TeamRole.MEMBER)
                .joinDate(LocalDate.now())
                .build();

        teamUserRepository.save(teamUser);

        List<UUID> memberIds = getTeamMembersId(teamId);

        UserJoinedTeamEvent event = UserJoinedTeamEvent.builder()
                .teamId(teamId)
                .userId(userId)
                .memberIds(memberIds)
                .build();

        teamEventPublisher.publishEvent(USER_JOINED_TOPIC, event);
    }

    private void removeMember(UUID teamId, UUID userId){
        Team team = teamRepository.findById(teamId).orElseThrow(
                ()-> new NotFoundException("Team does not exist")
        );

        int teamMembers = team.getTotalMembers() - 1;
        if(teamMembers == 0) {
            teamRepository.delete(team);
        }
        else{
            team.setTotalMembers(teamMembers);
            teamRepository.save(team);
        }

        TeamUser member = teamUserRepository.findByUserIdAndTeamId(userId, teamId);

        teamUserRepository.delete(member);

        List<UUID> memberIds = getTeamMembersId(teamId);

        UserLeftTeamEvent event = UserLeftTeamEvent.builder()
                .teamId(teamId)
                .userId(userId)
                .memberIds(memberIds)
                .build();

        teamEventPublisher.publishEvent(USER_LEFT_TOPIC, event);
    }
}
