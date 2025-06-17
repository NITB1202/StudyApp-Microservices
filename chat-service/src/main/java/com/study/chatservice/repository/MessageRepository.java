package com.study.chatservice.repository;

import com.study.chatservice.entity.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {
    @Query("""
    SELECT COUNT(m)
    FROM Message m
    WHERE m.teamId = :teamId
      AND m.userId <> :userId
      AND m.isDeleted = false
      AND m.id NOT IN (
          SELECT rs.messageId
          FROM MessageReadStatus rs
          WHERE rs.userId = :userId
      )
    """)
    long countUnreadMessages(
            @Param("userId") UUID userId,
            @Param("teamId") UUID teamId
    );
    List<Message> findByTeamId(UUID teamId, Pageable pageable);
    List<Message> findByTeamIdAndCreatedAtLessThan(UUID teamId, LocalDateTime createdAt, Pageable pageable);
    long countByTeamId(UUID teamId);
    void deleteAllByTeamId(UUID teamId);
}