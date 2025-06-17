package com.study.chatservice.repository;

import com.study.chatservice.entity.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {
    List<Message> findByTeamId(UUID teamId, Pageable pageable);
    List<Message> findByTeamIdAndCreatedAtLessThan(UUID teamId, LocalDateTime createdAt, Pageable pageable);
    long countByTeamId(UUID teamId);
    void deleteAllByTeamId(UUID teamId);
}