package com.study.teamservice.service;

import com.study.teamservice.entity.Team;
import com.study.teamservice.grpc.CreateTeamRequest;
import com.study.teamservice.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
    private final CodeService codeService;

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
}
