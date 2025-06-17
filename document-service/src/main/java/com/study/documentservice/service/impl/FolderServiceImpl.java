package com.study.documentservice.service.impl;

import com.study.common.exceptions.BusinessException;
import com.study.common.exceptions.NotFoundException;
import com.study.documentservice.entity.Folder;
import com.study.documentservice.grpc.*;
import com.study.documentservice.repository.FolderRepository;
import com.study.documentservice.service.FolderService;
import com.study.documentservice.service.UsageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FolderServiceImpl implements FolderService {
    private final FolderRepository folderRepository;
    private final UsageService usageService;
    private static final int DEFAULT_SIZE = 10;

    @Override
    public boolean isFolderCreator(IsFolderCreatorRequest request) {
        UUID folderId = UUID.fromString(request.getFolderId());
        UUID userId = UUID.fromString(request.getUserId());

        Folder folder = folderRepository.findById(folderId).orElseThrow(
                () -> new NotFoundException("Folder not found.")
        );

        return folder.getCreatedBy().equals(userId);
    }

    @Override
    public UUID isTeamFolder(IsTeamFolderRequest request) {
        UUID id = UUID.fromString(request.getId());

        Folder folder = folderRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Folder not found.")
        );

        return folder.getTeamId();
    }

    @Override
    public Folder createFolder(CreateFolderRequest request) {
        UUID userId = UUID.fromString(request.getUserId());
        UUID teamId = request.getTeamId().isEmpty() ? null : UUID.fromString(request.getTeamId());
        LocalDateTime now = LocalDateTime.now();

        if(request.getName().isEmpty()) {
            throw new BusinessException("Folder name cannot be empty.");
        }

        if(isPersonalFolderNameDuplicated(teamId, userId, request.getName()) ||
                isTeamFolderNameDuplicated(teamId, request.getName())) {
            throw new BusinessException("Folder already exists.");
        }

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
    public long getFolderCount(GetFoldersRequest request) {
        if(request.getTeamId().isEmpty()) {
            UUID userId = UUID.fromString(request.getUserId());
            return folderRepository.countByCreatedByAndTeamIdIsNull(userId);
        }
        UUID teamId = UUID.fromString(request.getTeamId());
        return folderRepository.countByTeamId(teamId);
    }

    @Override
    public long getFolderCountWithKeyword(SearchFolderByNameRequest request) {
        String handledKeyword = request.getKeyword().trim().toLowerCase();

        if(request.getTeamId().isEmpty()) {
            UUID userId = UUID.fromString(request.getUserId());
            return folderRepository.countByCreatedByAndTeamIdIsNullAndNameContainingIgnoreCase(userId, handledKeyword);
        }

        UUID teamId = UUID.fromString(request.getTeamId());
        return folderRepository.countByTeamIdAndNameContainingIgnoreCase(teamId, handledKeyword);
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

        if(isPersonalFolderNameDuplicated(folder.getTeamId(), userId, request.getName()) ||
                isTeamFolderNameDuplicated(folder.getTeamId(), request.getName())) {
            throw new BusinessException("Folder already exists.");
        }

        folder.setName(request.getName());
        folder.setUpdatedBy(userId);
        folder.setUpdatedAt(LocalDateTime.now());

        folderRepository.save(folder);
    }

    @Override
    @Transactional
    public void deleteFolder(DeleteFolderRequest request) {
        UUID id = UUID.fromString(request.getId());

        Folder folder = folderRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Folder not found.")
        );

        decreaseUsage(folder.getTeamId(), folder.getCreatedBy(), folder.getBytes());

        folderRepository.delete(folder);
    }

    @Override
    public boolean existsById(UUID id) {
        return folderRepository.existsById(id);
    }

    @Override
    public boolean validateMoveDocument(UUID oldFolderId, UUID newFolderId) {
        if(oldFolderId.equals(newFolderId)) return false;

        Folder oldFolder = folderRepository.findById(oldFolderId).orElseThrow(
                ()-> new NotFoundException("Folder with id " + oldFolderId + " doesn't exist.")
        );

        Folder newFolder = folderRepository.findById(newFolderId).orElseThrow(
                () -> new NotFoundException("Folder with id " + newFolderId + " doesn't exist.")
        );

        if (!Objects.equals(oldFolder.getTeamId(), newFolder.getTeamId())) {
            return false;
        }

        return oldFolder.getTeamId() != null || oldFolder.getCreatedBy().equals(newFolder.getCreatedBy());
    }

    @Override
    public void addDocument(UUID folderId, UUID userId, long bytes) {
        Folder folder = folderRepository.findById(folderId).orElseThrow(
                () -> new NotFoundException("Folder not found.")
        );

        increaseUsage(folder.getTeamId(), folder.getCreatedBy(), bytes);

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

        decreaseUsage(folder.getTeamId(), folder.getCreatedBy(), bytes);

        folder.setDocumentCount(folder.getDocumentCount() - 1);
        folder.setBytes(folder.getBytes() - bytes);
        folder.setUpdatedBy(userId);
        folder.setUpdatedAt(LocalDateTime.now());
        folderRepository.save(folder);
    }

    @Override
    public List<UUID> getAllIdsByTeamId(UUID teamId) {
        List<Folder> folders = folderRepository.findByTeamId(teamId);
        return folders.stream().map(Folder::getId).toList();
    }

    @Override
    public void deleteAllById(List<UUID> ids) {
        folderRepository.deleteAllById(ids);
    }

    private boolean isPersonalFolderNameDuplicated(UUID teamId, UUID userId, String name) {
        return teamId == null && folderRepository.existsByNameAndCreatedByAndTeamIdIsNull(name, userId);
    }

    private boolean isTeamFolderNameDuplicated(UUID teamId, String name) {
        return teamId != null && folderRepository.existsByNameAndTeamId(name, teamId);
    }

    private void increaseUsage(UUID teamId, UUID creatorId, long bytes) {
        if(teamId == null) {
            usageService.increaseUserUsage(creatorId, bytes);
        }
        else {
            usageService.increaseTeamUsage(teamId, bytes);
        }
    }

    private void decreaseUsage(UUID teamId, UUID creatorId, long bytes) {
        if(teamId == null) {
            usageService.decreaseUserUsage(creatorId, bytes);
        }
        else {
            usageService.decreaseTeamUsage(teamId, bytes);
        }
    }
}
