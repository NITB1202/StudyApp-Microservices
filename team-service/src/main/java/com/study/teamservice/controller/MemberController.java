package com.study.teamservice.controller;

import com.study.teamservice.entity.Invitation;
import com.study.teamservice.grpc.CreateInvitationRequest;
import com.study.teamservice.grpc.InvitationResponse;
import com.study.teamservice.grpc.TeamServiceGrpc;
import com.study.teamservice.mapper.InvitationMapper;
import com.study.teamservice.service.MemberService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class MemberController extends TeamServiceGrpc.TeamServiceImplBase {
    private final MemberService memberService;

    @Override
    public void createInvitation(CreateInvitationRequest request, StreamObserver<InvitationResponse> responseObserver){
        Invitation invitation = memberService.createInvitation(request);
        InvitationResponse response = InvitationMapper.toInvitationResponse(invitation);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
