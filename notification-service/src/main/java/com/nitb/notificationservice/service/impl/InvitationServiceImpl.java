package com.nitb.notificationservice.service.impl;

import com.nitb.notificationservice.entity.Invitation;
import com.nitb.notificationservice.repository.InvitationRepository;
import com.nitb.notificationservice.service.InvitationService;
import com.study.notificationservice.grpc.GetInvitationsRequest;
import com.study.notificationservice.grpc.ReplyToInvitationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvitationServiceImpl implements InvitationService {
    private final InvitationRepository invitationRepository;

    @Override
    public void createInvitation(UUID inviterId, UUID inviteeId, UUID teamId) {

    }

    @Override
    public List<Invitation> getInvitations(GetInvitationsRequest request) {
        return List.of();
    }

    @Override
    public void replyToInvitation(ReplyToInvitationRequest request) {

    }
}
