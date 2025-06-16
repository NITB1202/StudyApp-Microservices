package com.study.documentservice.service.impl;

import com.study.common.exceptions.BusinessException;
import com.study.common.exceptions.NotFoundException;
import com.study.documentservice.entity.Folder;
import com.study.documentservice.grpc.*;
import com.study.documentservice.repository.FolderRepository;
import com.study.documentservice.service.FolderService;
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
public class FolderServiceImpl implements FolderService {
    private final FolderRepository folderRepository;
    private static final int DEFAULT_SIZE = 10;

    @Override
    public Folder createFolder(CreateFolderRequest request) {
        if(request.getName().isEmpty()) {
            throw new BusinessException("Folder name cannot be empty.");
        }

        UUID userId = UUID.fromString(request.getUserId());
        UUID teamId = request.getTeamId().isEmpty() ? null : UUID.fromString(request.getTeamId());
        LocalDateTime now = LocalDateTime.now();

        Folder folder = Folder.builder()
                .name(request.getName())
                .createdBy(userId)
                .createdAt(now)
                .updatedBy(userId)
                .updatedAt(now)
                .bytes(0L)
                .documentCount(0)
                .teamId(teamId)
                .build();

        return folderRepository.save(folder);
    }

    @Override
    public Folder getFolderById(GetFolderByIdRequest request) {
        UUID id = UUID.fromString(request.getId());
        return folderRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Folder not found.")
        );
    }

    @Override
    public List<Folder> getFolders(GetFoldersRequest request) {
        int size = request.getSize() > 0 ? request.getSize() : DEFAULT_SIZE;
        Pageable pageable = PageRequest.of(0, size, Sort.by("createdAt").descending());

        if(request.getCursor().isEmpty()) {
            if(request.getTeamId().isEmpty()) {
                UUID userId = UUID.fromString(request.getUserId());
                return folderRepository.findByCreatedByAndTeamIdIsNull(userId, pageable);
            }
            else {
                UUID teamId = UUID.fromString(request.getTeamId());
                return folderRepository.findByTeamId(teamId, pageable);
            }
        }

        LocalDateTime cursor = LocalDateTime.parse(request.getCursor());

        if(request.getTeamId().isEmpty()) {
            UUID userId = UUID.fromString(request.getUserId());
            return folderRepository.findByCreatedByAndTeamIdIsNullAndCreatedAtLessThan(userId, cursor, pageable);
        }
        else {
            UUID teamId = UUID.fromString(request.getTeamId());
            return folderRepository.findByTeamIdAndCreatedAtLessThan(teamId, cursor, pageable);
        }
    }

    @Override
    public List<Folder> searchFolderByName(SearchFolderByNameRequest request) {
        int size = request.getSize() > 0 ? request.getSize() : DEFAULT_SIZE;
        Pageable pageable = PageRequest.of(0, size, Sort.by("createdAt").descending());
        String handledKeyword = request.getKeyword().trim().toLowerCase();

        if(request.getCursor().isEmpty()) {
            if(request.getTeamId().isEmpty()) {
                UUID userId = UUID.fromString(request.getUserId());
                return folderRepository.findByCreatedByAndTeamIdIsNullAndNameContainingIgnoreCase(userId, handledKeyword, pageable);
            }
            else {
                UUID teamId = UUID.fromString(request.getTeamId());
                return folderRepository.findByTeamIdAndNameContainingIgnoreCase(teamId, handledKeyword, pageable);
            }
        }

        LocalDateTime cursor = LocalDateTime.parse(request.getCursor());

        if(request.getTeamId().isEmpty()) {
            UUID userId = UUID.fromString(request.getUserId());
            return folderRepository.findByCreatedByAndTeamIdIsNullAndNameContainingIgnoreCaseAndCreatedAtLessThan(userId, handledKeyword, cursor, pageable);
        }
        else {
            UUID teamId = UUID.fromString(request.getTeamId());
            return folderRepository.findByTeamIdAndNameContainingIgnoreCaseAndCreatedAtLessThan(teamId, handledKeyword, cursor, pageable);
        }
    }

    @Override
    public int getUserFolderCount(UUID userId) {
        return folderRepository.countByCreatedByAndTeamIdIsNull(userId);
    }

    @Override
    public int getTeamFolderCount(UUID teamId) {
        return folderRepository.countByTeamId(teamId);
    }

    @Override
    public int getUserFolderCountWithKeyword(UUID userId, String keyword) {
        return folderRepository.countByCreatedByAndTeamIdIsNullAndNameContainingIgnoreCase(userId, keyword);
    }

    @Override
    public int getTeamFolderCountWithKeyword(UUID teamId, String keyword) {
        return folderRepository.countByTeamIdAndNameContainingIgnoreCase(teamId, keyword);
    }

    @Override
    public void updateFolderName(UpdateFolderNameRequest request) {
        UUID id = UUID.fromString(request.getId());
        UUID userId = UUID.fromString(request.getUserId());

        Folder folder = folderRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Folder not found.")
        );

        if(request.getName().isEmpty()) {
            throw new BusinessException("Folder name cannot be empty.");
        }

        folder.setName(request.getName());
        folder.setUpdatedBy(userId);
        folder.setUpdatedAt(LocalDateTime.now());

        folderRepository.save(folder);
    }

    @Override
    public void deleteFolder(DeleteFolderRequest request) {
        UUID id = UUID.fromString(request.getId());

        Folder folder = folderRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Folder not found.")
        );

        folderRepository.delete(folder);
    }

    @Override
    public void addDocument(UUID folderId, UUID userId, long bytes) {
        Folder folder = folderRepository.findById(folderId).orElseThrow(
                () -> new NotFoundException("Folder not found.")
        );

        folder.setDocumentCount(folder.getDocumentCount() + 1);
        folder.setBytes(folder.getBytes() + bytes);
        folder.setUpdatedBy(userId);
        folder.setUpdatedAt(LocalDateTime.now());
        folderRepository.save(folder);
    }

    @Override
    public void removeDocument(UUID folderId, UUID userId, long bytes) {
        Folder folder = folderRepository.findById(folderId).orElseThrow(
                () -> new NotFoundException("Folder not found.")
        );

        folder.setDocumentCount(folder.getDocumentCount() - 1);
        folder.setBytes(folder.getBytes() - bytes);
        folder.setUpdatedBy(userId);
        folder.setUpdatedAt(LocalDateTime.now());
        folderRepository.save(folder);
    }
}
