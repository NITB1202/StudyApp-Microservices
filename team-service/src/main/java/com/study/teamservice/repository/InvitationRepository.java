package com.study.teamservice.repository;

import com.study.teamservice.entity.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InvitationRepository extends JpaRepository<Invitation, UUID> {
    boolean existsByTeamIdAndInviteeId(UUID teamId, UUID inviteeId);
}