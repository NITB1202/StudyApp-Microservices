package com.nitb.notificationservice.service.impl;

import com.nitb.notificationservice.entity.Invitation;
import com.nitb.notificationservice.repository.InvitationRepository;
import com.nitb.notificationservice.service.InvitationService;
import com.study.common.exceptions.BusinessException;
import com.study.common.exceptions.NotFoundException;
import com.study.notificationservice.grpc.GetInvitationsRequest;
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
    private static final int DEFAULT_SIZE = 10;

    @Override
    public void createInvitation(String inviterName, UUID inviteeId, UUID teamId, String teamName) {
        if(invitationRepository.existsByInviteeIdAndTeamId(inviteeId, teamId)) {
            throw new BusinessException("The invitation has already been sent to the invitee.");
        }

        Invitation invitation = Invitation.builder()
                .inviterName(inviterName)
                .inviteeId(inviteeId)
                .teamId(teamId)
                .teamName(teamName)
                .invitedAt(LocalDateTime.now())
                .build();

        invitationRepository.save(invitation);
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
    public void deleteInvitation(UUID id) {
        Invitation invitation = invitationRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Invitation not found.")
        );

        invitationRepository.delete(invitation);
    }
}
