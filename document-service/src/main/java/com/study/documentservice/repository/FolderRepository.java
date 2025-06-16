package com.study.documentservice.repository;

import com.study.documentservice.entity.Folder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface FolderRepository extends JpaRepository<Folder, UUID> {
    boolean existsByNameAndCreatedByAndTeamIdIsNull(String name, UUID createdBy);
    boolean existsByNameAndTeamId(String name, UUID teamId);
    List<Folder> findByCreatedByAndTeamIdIsNull(UUID userId, Pageable pageable);
    List<Folder> findByTeamId(UUID teamId, Pageable pageable);
    List<Folder> findByCreatedByAndTeamIdIsNullAndCreatedAtLessThan(UUID userId, LocalDateTime createdAt, Pageable pageable);
    List<Folder> findByTeamIdAndCreatedAtLessThan(UUID teamId, LocalDateTime createdAt, Pageable pageable);
    List<Folder> findByCreatedByAndTeamIdIsNullAndNameContainingIgnoreCase(UUID userId, String name, Pageable pageable);
    List<Folder> findByTeamIdAndNameContainingIgnoreCase(UUID teamId, String name, Pageable pageable);
    List<Folder> findByCreatedByAndTeamIdIsNullAndNameContainingIgnoreCaseAndCreatedAtLessThan(UUID userId, String name, LocalDateTime createdAt, Pageable pageable);
    List<Folder> findByTeamIdAndNameContainingIgnoreCaseAndCreatedAtLessThan(UUID teamId, String name, LocalDateTime createdAt, Pageable pageable);
    long countByCreatedByAndTeamIdIsNull(UUID userId);
    long countByTeamId(UUID teamId);
    long countByCreatedByAndTeamIdIsNullAndNameContainingIgnoreCase(UUID userId, String name);
    long countByTeamIdAndNameContainingIgnoreCase(UUID teamId, String name);
}