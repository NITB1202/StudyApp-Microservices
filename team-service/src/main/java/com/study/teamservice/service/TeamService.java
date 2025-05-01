package com.study.teamservice.service;

import com.study.common.exceptions.BusinessException;
import com.study.common.exceptions.NotFoundException;
import com.study.teamservice.entity.Team;
import com.study.teamservice.grpc.*;
import com.study.teamservice.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
    private final CodeService codeService;

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

    public Team getTeamById(UUID teamId) {
        return teamRepository.findById(teamId).orElseThrow(
                () -> new NotFoundException("Team not found.")
        );
    }

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

    public void deleteTeam(UUID teamId) {
        if(!teamRepository.existsById(teamId))
            throw new NotFoundException("Team not found");

        teamRepository.deleteById(teamId);
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

}
