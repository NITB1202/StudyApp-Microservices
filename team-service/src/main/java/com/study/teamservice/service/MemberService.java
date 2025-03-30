package com.study.teamservice.service;

import com.study.common.enums.InvitationStatus;
import com.study.common.exceptions.BusinessException;
import com.study.common.exceptions.NotFoundException;
import com.study.teamservice.entity.Invitation;
import com.study.teamservice.grpc.CreateInvitationRequest;
import com.study.teamservice.repository.InvitationRepository;
import com.study.teamservice.repository.TeamUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final InvitationRepository invitationRepository;
    private final TeamUserRepository teamUserRepository;

    public Invitation createInvitation(CreateInvitationRequest request) {
        UUID teamId = UUID.fromString(request.getTeamId());
        UUID inviterId = UUID.fromString(request.getInviterId());
        UUID inviteeId = UUID.fromString(request.getInviteeId());

        if(!teamUserRepository.existsByUserIdAndTeamId(inviterId, teamId)) {
            throw new NotFoundException("User is not in this team");
        }

        if(invitationRepository.existsByTeamIdAndInviteeId(teamId, inviteeId)){
            throw new BusinessException("Invitation already exists");
        }

        Invitation invitation = Invitation.builder()
                .teamId(teamId)
                .inviterId(inviterId)
                .inviteeId(inviteeId)
                .createdAt(LocalDateTime.now())
                .status(InvitationStatus.PENDING)
                .build();

        return invitationRepository.save(invitation);
    }


}
