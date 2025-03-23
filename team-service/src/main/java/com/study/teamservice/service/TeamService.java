package com.study.teamservice.service;

import com.study.common.exceptions.NotFoundException;
import com.study.teamservice.entity.Team;
import com.study.teamservice.entity.TeamUser;
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
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
    private final TeamUserRepository teamUserRepository;
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
}
