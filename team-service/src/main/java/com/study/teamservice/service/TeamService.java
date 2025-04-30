package com.study.teamservice.service;

import com.study.common.enums.TeamRole;
import com.study.common.exceptions.BusinessException;
import com.study.common.exceptions.NotFoundException;
import com.study.teamservice.entity.Team;
import com.study.teamservice.entity.TeamUser;
import com.study.common.events.Team.TeamDeletedEvent;
import com.study.teamservice.event.TeamEventPublisher;
import com.study.common.events.Team.TeamUpdatedEvent;
import com.study.teamservice.grpc.*;
import com.study.teamservice.mapper.TeamMapper;
import com.study.teamservice.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
    private final CodeService codeService;
    private final MemberService memberService;
    private final TeamEventPublisher teamEventPublisher;

    private static final int DEFAULT_SIZE = 10;
    private static final String UPDATE_TOPIC = "team-updated";
    private static final String DELETE_TOPIC = "team-deleted";

    public Team createTeam(CreateTeamRequest request) {
        UUID userId = UUID.fromString(request.getCreatorId());

        if(teamRepository.existsByNameAndCreatorId(request.getName(), userId)){
            throw new BusinessException("User has already created a team with the same name.");
        }

        int retry = 0;

        while(true){
            String randomCode = codeService.generateRandomCode();

            try{
                Team team = Team.builder()
                        .name(request.getName())
                        .description(request.getDescription())
                        .teamCode(randomCode)
                        .createDate(LocalDate.now())
                        .creatorId(userId)
                        .totalMembers(1)
                        .avatarUrl("")
                        .build();

                teamRepository.save(team);

                memberService.createUserTeam(team.getId(), userId, TeamRole.CREATOR);

                return team;
            }
            catch (DataIntegrityViolationException e){
                retry++;
                System.out.println("Retry: " + retry);
            }
        }
    }

    public Team getTeamById(GetTeamByIdRequest request) {
        return teamRepository.findById(UUID.fromString(request.getId())).orElseThrow(
                () -> new NotFoundException("Team not found")
        );
    }

    public List<Team> getUserTeams(GetUserTeamsRequest request) {
        //Validate request
        int size = request.getSize() > 0 ? request.getSize() : DEFAULT_SIZE;
        LocalDate cursor = request.getCursor().isEmpty() ? null : LocalDate.parse(request.getCursor());
        UUID userId = UUID.fromString(request.getUserId());

        Pageable pageable = PageRequest.of(0, size, Sort.by("joinDate").descending());

        //Get result
        List<TeamUser> teamUsers = memberService.getUserTeamsByCursor(userId, cursor, pageable);

        //Get teams with unchanged position
        List<UUID> teamIds = teamUsers.stream().map(TeamUser::getTeamId).toList();
        Map<UUID, Team> teamMap = teamRepository.findAllById(teamIds).stream()
                .collect(Collectors.toMap(Team::getId, team -> team));

        return teamIds.stream()
                .map(teamMap::get)
                .filter(Objects::nonNull)
                .toList();
    }

    public List<Team> searchUserTeamByName(SearchUserTeamByNameRequest request) {
        int size = request.getSize() > 0 ? request.getSize() : DEFAULT_SIZE;
        LocalDate cursor = request.getCursor().isEmpty() ? null : LocalDate.parse(request.getCursor());
        UUID userId = UUID.fromString(request.getUserId());
        String keyword = "%" + request.getKeyword().trim() + "%";

        Pageable pageable = PageRequest.of(0, size, Sort.by("tu.joinDate").descending());

        return cursor != null ?
                teamRepository.searchTeamsByUserAndNameWithCursor(userId, keyword, cursor, pageable) :
                teamRepository.searchTeamsByUserAndName(userId, keyword, pageable);
    }

    public Team updateTeam(UpdateTeamRequest request) {
        UUID teamId = UUID.fromString(request.getId());
        UUID userId = UUID.fromString(request.getUserId());

        Team team = teamRepository.findById(teamId).orElseThrow(
                () -> new NotFoundException("Team not found")
        );

        if(memberService.userDoesNotHavePermissionToUpdate(userId, teamId))
            throw new BusinessException("User doesn't have permission to update this team");

        Set<String> updatedFields = new HashSet<>();

        if(!request.getName().isBlank()) {
            if(request.getName().equals(team.getName()))
                throw new BusinessException("The new name is the same as the old one");

            if(teamRepository.existsByNameAndCreatorId(request.getName(), userId)){
                throw new BusinessException("User has already created a team with the same name.");
            }

            team.setName(request.getName());
            updatedFields.add("name");
        }

        if(!request.getDescription().isBlank()) {
            team.setDescription(request.getDescription());
            updatedFields.add("description");
        }

        if(!updatedFields.isEmpty()) {
            List<UUID> memberIds = memberService.getTeamMembersId(teamId);

            TeamUpdatedEvent event = TeamUpdatedEvent.builder()
                    .id(teamId)
                    .updatedBy(userId)
                    .updatedFields(updatedFields)
                    .memberIds(memberIds)
                    .build();

            teamEventPublisher.publishEvent(UPDATE_TOPIC, event);
        }

        return teamRepository.save(team);
    }

    public void uploadTeamAvatar(UploadTeamAvatarRequest request) {
        UUID teamId = UUID.fromString(request.getTeamId());
        UUID userId = UUID.fromString(request.getUserId());

        Team team = teamRepository.findById(teamId).orElseThrow(
                () -> new NotFoundException("Team not found")
        );

        if(memberService.userDoesNotHavePermissionToUpdate(userId, teamId))
            throw new BusinessException("User doesn't have permission to update this team");

        if(request.getAvatarUrl().isBlank())
            throw new BusinessException("The avatar url is empty");

        team.setAvatarUrl(request.getAvatarUrl());

        Set<String> updatedFields = Set.of("avatar");
        List<UUID> memberIds = memberService.getTeamMembersId(teamId);

        TeamUpdatedEvent event = TeamUpdatedEvent.builder()
                .id(teamId)
                .updatedBy(userId)
                .updatedFields(updatedFields)
                .memberIds(memberIds)
                .build();

        teamEventPublisher.publishEvent(UPDATE_TOPIC, event);

        teamRepository.save(team);
    }

    public void deleteTeam(DeleteTeamRequest request) {
        UUID teamId = UUID.fromString(request.getId());
        UUID userId = UUID.fromString(request.getUserId());

        if(!teamRepository.existsById(teamId))
            throw new NotFoundException("Team not found");

        TeamUser teamUser = memberService.getByUserIdAndTeamId(userId, teamId);

        if(teamUser == null)
            throw new NotFoundException("User is not part of this team");

        if(teamUser.getRole() != TeamRole.CREATOR)
            throw new BusinessException("User doesn't have permission to delete this team");

        teamRepository.deleteById(teamId);

        List<UUID> memberIds = memberService.getTeamMembersId(teamId);

        TeamDeletedEvent event = TeamDeletedEvent.builder()
                .id(teamId)
                .deletedBy(userId)
                .memberIds(memberIds)
                .build();

        teamEventPublisher.publishEvent(DELETE_TOPIC, event);
    }


    public long countUserTeamByKeyword(UUID userId, String keyword){
        return teamRepository.countUserTeamsByKeyword(userId, keyword);
    }

    public boolean existsById(UUID teamId) {
        return teamRepository.existsById(teamId);
    }

    public Team getByTeamCode(String teamCode) {
        return teamRepository.findByTeamCode(teamCode);
    }

    public void increaseMember(UUID teamId) {
        Team team = teamRepository.findById(teamId).orElseThrow(
                ()-> new NotFoundException("Team does not exist")
        );

        team.setTotalMembers(team.getTotalMembers() + 1);
        teamRepository.save(team);
    }

    public void decreaseMember(UUID teamId) {
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
    }

    public List<TeamSummaryResponse> toListTeamSummaryResponse(List<Team> teams, UUID userId) {
        List<TeamSummaryResponse> teamResponses = new ArrayList<>();

        for (Team team : teams) {
            boolean managedByUser = memberService.isTeamManagedByUser(team.getId(), userId);
            TeamSummaryResponse teamSummaryResponse = TeamMapper.toTeamSummaryResponse(team, managedByUser);
            teamResponses.add(teamSummaryResponse);
        }

        return teamResponses;
    }
}
