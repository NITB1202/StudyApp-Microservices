package com.study.apigateway.grpcclient;

import com.study.teamservice.grpc.TeamResponse;
import com.study.teamservice.grpc.TeamServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class TeamServiceGrpcClient {
    @GrpcClient("team-service")
    private TeamServiceGrpc.TeamServiceBlockingStub stub;


}
