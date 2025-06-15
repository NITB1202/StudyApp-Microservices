package com.study.notificationservice.service.impl;

import com.study.notificationservice.dto.CreateInvitationDto;
import com.study.notificationservice.entity.Invitation;
import com.study.notificationservice.event.NotificationEventPublisher;
import com.study.notificationservice.repository.InvitationRepository;
import com.study.notificationservice.service.InvitationService;
import com.study.common.events.Notification.InvitationAcceptEvent;
import com.study.common.exceptions.BusinessException;
import com.study.common.exceptions.NotFoundException;
import com.study.notificationservice.grpc.GetInvitationsRequest;
import com.study.notificationservice.grpc.ReplyToInvitationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvitationServiceImpl implements InvitationService {
    private final InvitationRepository invitationRepository;
    private final NotificationEventPublisher publisher;
    private static final int DEFAULT_SIZE = 10;
    private static final String ACCEPT_TOPIC = "invitation-accepted";

    @Override
    public UUID createInvitation(CreateInvitationDto request) {
        if(invitationRepository.existsByInviteeIdAndTeamId(request.getInviteeId(), request.getTeamId())) {
            throw new BusinessException("The invitation has already been sent to the invitee.");
        }

        Invitation invitation = Invitation.builder()
                .inviterName(request.getInviterName())
                .inviterAvatarUrl(request.getInviterAvatarUrl())
                .inviteeId(request.getInviteeId())
                .teamId(request.getTeamId())
                .teamName(request.getTeamName())
                .invitedAt(LocalDateTime.now())
                .build();

        invitationRepository.save(invitation);

        return invitation.getId();
    }

    @Override
    public List<Invitation> getInvitations(GetInvitationsRequest request) {
        UUID userId = UUID.fromString(request.getUserId());
        int size = request.getSize() > 0 ? request.getSize() : DEFAULT_SIZE;

        Pageable pageable = PageRequest.of(0, size, Sort.by("invitedAt").descending());

        if(request.getCursor().isEmpty()) {
            return invitationRepository.findByInviteeId(userId, pageable);
        }

        LocalDateTime cursor = LocalDateTime.parse(request.getCursor());
        return invitationRepository.findByInviteeIdAndInvitedAtLessThan(userId, cursor, pageable);
    }

    @Override
    public void replyToInvitation(ReplyToInvitationRequest request) {
        UUID id = UUID.fromString(request.getId());

        Invitation invitation = invitationRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Invitation not found.")
        );

        if(!request.getUserId().equals(invitation.getInviteeId().toString())) {
            throw new BusinessException("You are not allowed to reply this invitation.");
        }

        if(request.getAccept()) {
            InvitationAcceptEvent event = InvitationAcceptEvent.builder()
                    .teamId(invitation.getTeamId())
                    .userId(invitation.getInviteeId())
                    .build();

            publisher.publishEvent(ACCEPT_TOPIC, event);
        }

        invitationRepository.delete(invitation);
    }

    @Override
    public long countInvitationsByUserId(UUID userId) {
        return invitationRepository.countByInviteeId(userId);
    }

    @Override
    public void deleteInvitationBefore(LocalDateTime time) {
        invitationRepository.deleteAllByInvitedAtBefore(time);
    }
}
