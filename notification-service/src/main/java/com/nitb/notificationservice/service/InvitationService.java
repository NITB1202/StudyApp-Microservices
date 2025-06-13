package com.nitb.notificationservice.service;

import com.nitb.notificationservice.dto.CreateInvitationDto;
import com.nitb.notificationservice.entity.Invitation;
import com.study.notificationservice.grpc.GetInvitationsRequest;
import com.study.notificationservice.grpc.ReplyToInvitationRequest;

import java.util.List;
import java.util.UUID;

public interface InvitationService {
    void createInvitation(CreateInvitationDto request);
    List<Invitation> getInvitations(GetInvitationsRequest request);
    void replyToInvitation(ReplyToInvitationRequest request);
    long countInvitationsByUserId(UUID userId);
}
