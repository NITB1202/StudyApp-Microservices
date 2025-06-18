package com.study.chatservice.grpc;

import com.study.teamservice.grpc.GetTeamByIdRequest;
import com.study.teamservice.grpc.TeamDetailResponse;
import com.study.teamservice.grpc.TeamServiceGrpc;
import com.study.teamservice.grpc.ValidateUsersInTeamRequest;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class TeamServiceGrpcClient {
    @GrpcClient("team-service")
    private TeamServiceGrpc.TeamServiceBlockingStub blockingStub;

    public TeamDetailResponse getTeamById(UUID id){
        GetTeamByIdRequest request = GetTeamByIdRequest.newBuilder()
                .setId(id.toString())
                .build();

        return blockingStub.getTeamById(request);
    }

    public void validateUsersInTeam(UUID teamId, Set<UUID> userIds) {
        List<String> idsStr = userIds.stream()
                .map(UUID::toString)
                .toList();

        ValidateUsersInTeamRequest request = ValidateUsersInTeamRequest.newBuilder()
                .setTeamId(teamId.toString())
                .addAllUserIds(idsStr)
                .build();

        blockingStub.validateUsersInTeam(request);
    }
}