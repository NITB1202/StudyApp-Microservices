package com.study.teamservice.service;

import com.study.common.enums.TeamRole;
import com.study.common.exceptions.BusinessException;
import com.study.common.exceptions.NotFoundException;
import com.study.teamservice.entity.Team;
import com.study.teamservice.entity.TeamUser;
import com.study.teamservice.event.TeamDeletedEvent;
import com.study.teamservice.event.TeamEventPublisher;
import com.study.teamservice.event.TeamUpdatedEvent;
import com.study.teamservice.grpc.*;
import com.study.teamservice.repository.TeamUserRepository;
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
    private final TeamUserRepository teamUserRepository;
    private final CodeService codeService;
    private final TeamEventPublisher teamEventPublisher;
    private static final String UPDATE_TOPIC = "team-updated";
    private static final String DELETE_TOPIC = "team-deleted";

    public Team createTeam(CreateTeamRequest request) {
        int retry = 0;
        String avatarUrl = request.getAvatarUrl().isEmpty() ? null : request.getAvatarUrl();

        while(true){
            String randomCode = codeService.generateRandomCode();

            try{
                Team team = Team.builder()
                        .name(request.getName())
                        .teamCode(randomCode)
                        .createDate(LocalDate.now())
                        .creatorId(UUID.fromString(request.getCreatorId()))
                        .totalMembers(1)
                        .avatarUrl(avatarUrl)
                        .build();

                return teamRepository.save(team);
            }
            catch (DataIntegrityViolationException e){
                retry++;
                System.out.println("Retry: " + retry);
            }
        }
    }

    public UUID getFirstTeamId(GetFirstTeamIdRequest request) {
        TeamUser teamUser = teamUserRepository.getFirstByUserIdOrderByJoinDateDesc(UUID.fromString(request.getUserId()));

        if(teamUser == null){
            throw new NotFoundException("User haven't joined any team");
        }

        return teamUser.getTeamId();
    }

    public Team getTeamById(GetTeamByIdRequest request) {
        return teamRepository.findById(UUID.fromString(request.getId())).orElseThrow(
                () -> new NotFoundException("Team not found")
        );
    }

    public List<Team> getUserTeams(GetUserTeamsRequest request) {
        //Validate request
        int size = request.getSize() > 0 ? request.getSize() : 10;
        LocalDate cursor = request.getCursor().isEmpty() ? null : LocalDate.parse(request.getCursor());
        UUID userId = UUID.fromString(request.getUserId());

        Pageable pageable = PageRequest.of(0, size, Sort.by("joinDate").descending());

        //Get result
        List<TeamUser> teamUsers = cursor != null ?
                teamUserRepository.findByUserIdAndJoinDateBeforeOrderByJoinDateDesc(userId, cursor, pageable) :
                teamUserRepository.findByUserIdOrderByJoinDateDesc(userId, pageable);

        //Get teams with unchanged position
        List<UUID> teamIds = teamUsers.stream().map(TeamUser::getTeamId).toList();
        Map<UUID, Team> teamMap = teamRepository.findAllById(teamIds).stream()
                .collect(Collectors.toMap(Team::getId, team -> team));

        return teamIds.stream()
                .map(teamMap::get)
                .filter(Objects::nonNull)
                .toList();
    }

    public long countUserTeam(UUID userId){
        return teamUserRepository.countByUserId(userId);
    }

    public List<Team> searchUserTeamByName(SearchUserTeamByNameRequest request) {
        int size = request.getSize() > 0 ? request.getSize() : 10;
        LocalDate cursor = request.getCursor().isEmpty() ? null : LocalDate.parse(request.getCursor());
        UUID userId = UUID.fromString(request.getUserId());
        String keyword = "%" + request.getKeyword().trim() + "%";

        Pageable pageable = PageRequest.of(0, size, Sort.by("tu.joinDate").descending());

        return cursor != null ?
                teamRepository.searchTeamsByUserAndNameWithCursor(userId, keyword, cursor, pageable) :
                teamRepository.searchTeamsByUserAndName(userId, keyword, pageable);
    }

    public long countUserTeamByKeyword(UUID userId, String keyword){
        return teamRepository.countUserTeamsByKeyword(userId, keyword);
    }

    public Team updateTeam(UpdateTeamRequest request) {
        Team team = teamRepository.findById(UUID.fromString(request.getId())).orElseThrow(
                () -> new NotFoundException("Team not found")
        );

        TeamUser teamUser = teamUserRepository.findByUserIdAndTeamId(
                UUID.fromString(request.getUserId()), UUID.fromString(request.getId()));

        if(teamUser == null)
            throw new NotFoundException("User is not part of this team");

        if(teamUser.getRole() == TeamRole.MEMBER)
            throw new BusinessException("User doesn't have permission to update this team");

        Set<String> updatedFields = new HashSet<>();

        if(!request.getName().isBlank()) {
            team.setName(request.getName());
            updatedFields.add("name");
        }

        if(!request.getAvatarUrl().isBlank()) {
            team.setAvatarUrl(request.getAvatarUrl());
            updatedFields.add("avatarUrl");
        }

        if(!updatedFields.isEmpty()) {
            List<UUID> memberIds = teamUserRepository.findByTeamId(team.getId()).stream()
                    .map(TeamUser::getUserId)
                    .toList();

            TeamUpdatedEvent event = TeamUpdatedEvent.builder()
                    .id(team.getId())
                    .updatedBy(UUID.fromString(request.getUserId()))
                    .name(team.getName())
                    .avatarUrl(team.getAvatarUrl())
                    .updatedFields(updatedFields)
                    .memberIds(memberIds)
                    .build();

            teamEventPublisher.publishEvent(UPDATE_TOPIC, event);
        }

        return teamRepository.save(team);
    }

    public void deleteTeam(DeleteTeamRequest request) {
        if(!teamRepository.existsById(UUID.fromString(request.getId())))
            throw new NotFoundException("Team not found");

        TeamUser teamUser = teamUserRepository.findByUserIdAndTeamId(
                UUID.fromString(request.getUserId()), UUID.fromString(request.getId()));

        if(teamUser == null)
            throw new NotFoundException("User is not part of this team");

        if(teamUser.getRole() != TeamRole.CREATOR)
            throw new BusinessException("User doesn't have permission to delete this team");

        teamRepository.deleteById(UUID.fromString(request.getId()));

        TeamDeletedEvent event = TeamDeletedEvent.builder()
                .id(UUID.fromString(request.getId()))
                .deletedBy(UUID.fromString(request.getUserId()))
                .build();

        teamEventPublisher.publishEvent(DELETE_TOPIC, event);
    }
}
