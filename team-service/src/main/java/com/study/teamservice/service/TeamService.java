package com.study.teamservice.service;

import com.study.teamservice.entity.Team;
import com.study.teamservice.grpc.*;

import java.util.List;
import java.util.UUID;

public interface TeamService {
    Team createTeam(CreateTeamRequest request);
    void updateTeam(UpdateTeamRequest request);
    void uploadTeamAvatar(UploadTeamAvatarRequest request);
    void deleteTeam(DeleteTeamRequest request);
    void resetTeamCode(ResetTeamCodeRequest request);

    boolean existsById(UUID id);
    String getTeamName(UUID teamId);
    Team getTeamById(UUID teamId);
    Team getTeamByTeamCode(String teamCode);
    List<Team> getTeamsByListOfIds(List<UUID> ids);
    void increaseMember(UUID teamId);
    void decreaseMember(UUID teamId);
}
