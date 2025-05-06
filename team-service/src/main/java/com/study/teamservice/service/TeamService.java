package com.study.teamservice.service;

import com.study.teamservice.entity.Team;
import com.study.teamservice.grpc.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface TeamService {
    Team createTeam(CreateTeamRequest request);
    Set<String> updateTeam(UpdateTeamRequest request);
    void uploadTeamAvatar(UploadTeamAvatarRequest request);
    void deleteTeam(DeleteTeamRequest request);

    boolean existsById(UUID id);
    Team getTeamById(UUID teamId);
    Team getTeamByTeamCode(String teamCode);
    List<Team> getTeamsByListOfIds(List<UUID> ids);
    void increaseMember(UUID teamId);
    void decreaseMember(UUID teamId);
}
