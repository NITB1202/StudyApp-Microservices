package com.study.teamservice.service.impl;

import com.study.common.exceptions.BusinessException;
import com.study.common.exceptions.NotFoundException;
import com.study.teamservice.entity.Team;
import com.study.teamservice.grpc.*;
import com.study.teamservice.repository.TeamRepository;
import com.study.teamservice.service.CodeService;
import com.study.teamservice.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {
    private final TeamRepository teamRepository;
    private final CodeService codeService;

    @Override
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
                        .totalMembers(0)
                        .avatarUrl("")
                        .build();

                teamRepository.save(team);

                return team;
            }
            catch (DataIntegrityViolationException e){
                retry++;
                System.out.println("Retry: " + retry);
            }
        }
    }

    @Override
    public Set<String> updateTeam(UpdateTeamRequest request) {
        UUID teamId = UUID.fromString(request.getId());
        UUID userId = UUID.fromString(request.getUserId());

        Team team = teamRepository.findById(teamId).orElseThrow(
                () -> new NotFoundException("Team not found")
        );

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

        teamRepository.save(team);

        return updatedFields;
    }

    @Override
    public void uploadTeamAvatar(UploadTeamAvatarRequest request) {
        UUID teamId = UUID.fromString(request.getTeamId());

        Team team = teamRepository.findById(teamId).orElseThrow(
                () -> new NotFoundException("Team not found")
        );

        if(request.getAvatarUrl().isBlank())
            throw new BusinessException("The avatar url is empty");

        team.setAvatarUrl(request.getAvatarUrl());

        teamRepository.save(team);
    }

    @Override
    public void deleteTeam(DeleteTeamRequest request) {
        UUID teamId = UUID.fromString(request.getId());

        if(!teamRepository.existsById(teamId))
            throw new NotFoundException("Team not found");

        teamRepository.deleteById(teamId);
    }

    @Override
    public boolean existsById(UUID teamId) {
        return teamRepository.existsById(teamId);
    }

    @Override
    public Team getTeamById(UUID teamId) {
        return teamRepository.findById(teamId).orElseThrow(
                () -> new NotFoundException("Team not found.")
        );
    }

    @Override
    public Team getTeamByTeamCode(String teamCode) {
        return teamRepository.findByTeamCode(teamCode);
    }

    @Override
    public List<Team> getTeamsByListOfIds(List<UUID> ids) {
        Map<UUID, Team> teamMap = teamRepository.findAllById(ids)
                .stream()
                .collect(Collectors.toMap(Team::getId, Function.identity()));

        return ids.stream()
                .map(teamMap::get)
                .collect(Collectors.toList());
    }

    @Override
    public void increaseMember(UUID teamId) {
        Team team = teamRepository.findById(teamId).orElseThrow(
                ()-> new NotFoundException("Team does not exist")
        );

        team.setTotalMembers(team.getTotalMembers() + 1);
        teamRepository.save(team);
    }

    @Override
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

}
