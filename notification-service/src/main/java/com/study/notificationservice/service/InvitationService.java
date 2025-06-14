package com.study.notificationservice.service;

import com.study.notificationservice.dto.CreateInvitationDto;
import com.study.notificationservice.entity.Invitation;
import com.study.notificationservice.grpc.GetInvitationsRequest;
import com.study.notificationservice.grpc.ReplyToInvitationRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface InvitationService {
    UUID createInvitation(CreateInvitationDto request);
    List<Invitation> getInvitations(GetInvitationsRequest request);
    void replyToInvitation(ReplyToInvitationRequest request);
    long countInvitationsByUserId(UUID userId);
    void deleteInvitationBefore(LocalDateTime time);
}
