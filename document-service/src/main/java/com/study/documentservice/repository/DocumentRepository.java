package com.study.documentservice.repository;

import com.study.documentservice.entity.Document;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface DocumentRepository extends JpaRepository<Document, UUID> {
    boolean existsByNameAndFolderId(String name, UUID folderId);
    List<Document> findByFolderId(UUID folderId, Pageable pageable);
    List<Document> findByFolderIdAndCreatedAtLessThan(UUID folderId, LocalDateTime createdAt, Pageable pageable);
    List<Document> findByFolderIdAndNameContainingIgnoreCase(UUID folderId, String name, Pageable pageable);
    List<Document> findByFolderIdAndNameContainingIgnoreCaseAndCreatedAtLessThan(UUID folderId, String name, LocalDateTime createdAt, Pageable pageable);
    long countByFolderId(UUID folderId);
    long countByFolderIdAndNameContainingIgnoreCase(UUID folderId, String name);
    void deleteAllByFolderId(UUID folderId);
}