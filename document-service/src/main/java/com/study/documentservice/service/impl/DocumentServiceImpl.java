package com.study.documentservice.service.impl;

import com.study.common.exceptions.BusinessException;
import com.study.common.exceptions.NotFoundException;
import com.study.documentservice.dto.FileResponseDto;
import com.study.documentservice.entity.Document;
import com.study.documentservice.grpc.*;
import com.study.documentservice.repository.DocumentRepository;
import com.study.documentservice.service.DocumentService;
import com.study.documentservice.service.FileService;
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
public class DocumentServiceImpl implements DocumentService {
    private final DocumentRepository documentRepository;
    private final FolderService folderService;
    private final FileService fileService;
    private final static String FOLDER_PATH = "folders";
    private final static int DEFAULT_SIZE = 10;

    @Override
    public Document uploadDocument(UploadDocumentRequest request) {
        UUID folderId = UUID.fromString(request.getFolderId());
        UUID userId = UUID.fromString(request.getUserId());
        LocalDateTime now = LocalDateTime.now();

        if(request.getName().isEmpty()) {
            throw new BusinessException("Document name cannot be empty.");
        }

        if(!folderService.existsById(folderId)) {
            throw new NotFoundException("Folder not found.");
        }

        if(documentRepository.existsByNameAndFolderId(request.getName(), folderId)) {
            throw new BusinessException("Document already exists.");
        }

        Document document = Document.builder()
                .name(request.getName())
                .folderId(folderId)
                .createdBy(userId)
                .createdAt(now)
                .updatedBy(userId)
                .updatedAt(now)
                .build();

        documentRepository.save(document);

        String folderPath = FOLDER_PATH + "/" + folderId;
        FileResponseDto file = fileService.uploadFile(folderPath, document.getId().toString(), request.getFile());

        folderService.addDocument(folderId, userId, file.getBytes());

        document.setUrl(file.getUrl());
        document.setBytes(file.getBytes());

        return documentRepository.save(document);
    }

    @Override
    public Document getDocumentById(GetDocumentByIdRequest request) {
        UUID id = UUID.fromString(request.getId());
        return documentRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Document not found.")
        );
    }

    @Override
    public List<Document> getDocuments(GetDocumentsRequest request) {
        UUID folderId = UUID.fromString(request.getFolderId());
        int size = request.getSize() > 0 ? request.getSize() : DEFAULT_SIZE;
        Pageable pageable = PageRequest.of(0, size, Sort.by("createdAt").descending());

        if(request.getCursor().isEmpty()) {
            return documentRepository.findByFolderId(folderId, pageable);
        }

        LocalDateTime cursor = LocalDateTime.parse(request.getCursor());
        return documentRepository.findByFolderIdAndCreatedAtLessThan(folderId, cursor, pageable);
    }

    @Override
    public List<Document> searchDocumentByName(SearchDocumentByNameRequest request) {
        UUID folderId = UUID.fromString(request.getFolderId());
        int size = request.getSize() > 0 ? request.getSize() : DEFAULT_SIZE;
        Pageable pageable = PageRequest.of(0, size, Sort.by("createdAt").descending());
        String handledKeyword = request.getKeyword().trim().toLowerCase();

        if(request.getCursor().isEmpty()) {
            return documentRepository.findByFolderIdAndNameContainingIgnoreCase(folderId, handledKeyword, pageable);
        }

        LocalDateTime cursor = LocalDateTime.parse(request.getCursor());
        return documentRepository.findByFolderIdAndNameContainingIgnoreCaseAndCreatedAtLessThan(folderId, handledKeyword, cursor, pageable);
    }

    @Override
    public long getDocumentCount(UUID folderId) {
        return documentRepository.countByFolderId(folderId);
    }

    @Override
    public long getDocumentCountWithKeyword(UUID folderId, String keyword) {
        String handledKeyword = keyword.trim().toLowerCase();
        return documentRepository.countByFolderIdAndNameContainingIgnoreCase(folderId, handledKeyword);
    }

    @Override
    public void updateDocumentName(UpdateDocumentNameRequest request) {
        UUID id = UUID.fromString(request.getId());
        UUID userId = UUID.fromString(request.getUserId());

        Document document = documentRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Document not found.")
        );

        if(request.getName().isEmpty()) {
            throw new BusinessException("Document name cannot be empty.");
        }

        if(documentRepository.existsByNameAndFolderId(request.getName(), document.getFolderId())) {
            throw new BusinessException("Name already exists.");
        }

        document.setName(request.getName());
        document.setUpdatedBy(userId);
        document.setUpdatedAt(LocalDateTime.now());

        documentRepository.save(document);
    }

    @Override
    public void moveDocument(MoveDocumentRequest request) {
        UUID id = UUID.fromString(request.getId());
        UUID userId = UUID.fromString(request.getUserId());
        UUID newFolderId = UUID.fromString(request.getNewFolderId());

        Document document = documentRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Document not found.")
        );

        if(!folderService.existsById(newFolderId)) {
            throw new NotFoundException("Folder not found.");
        }

        if(document.getFolderId().equals(newFolderId)) {
            throw new BusinessException("Document already exists in the folder.");
        }

        String folderPath = FOLDER_PATH + "/" + newFolderId;
        fileService.moveFile(id.toString(), folderPath);

        folderService.removeDocument(document.getFolderId(), userId, document.getBytes());
        folderService.addDocument(newFolderId, userId, document.getBytes());

        document.setFolderId(newFolderId);
        document.setUpdatedBy(userId);
        document.setUpdatedAt(LocalDateTime.now());

        documentRepository.save(document);
    }

    @Override
    public void deleteDocument(DeleteDocumentRequest request) {
        UUID id = UUID.fromString(request.getId());
        UUID userId = UUID.fromString(request.getUserId());

        Document document = documentRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Document not found.")
        );

        fileService.deleteFile(id.toString());
        folderService.removeDocument(document.getFolderId(), userId, document.getBytes());
        documentRepository.delete(document);
    }

    @Override
    public void deleteAllDocuments(UUID folderId) {
        documentRepository.deleteAllByFolderId(folderId);
    }
}
