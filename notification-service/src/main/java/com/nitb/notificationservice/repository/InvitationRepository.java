package com.nitb.notificationservice.repository;

import com.nitb.notificationservice.entity.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InvitationRepository extends JpaRepository<Invitation, UUID> {
}