package com.study.documentservice.service;

import com.study.documentservice.entity.Document;
import com.study.documentservice.grpc.*;

import java.util.List;
import java.util.UUID;

public interface DocumentService {
    Document uploadDocument(UploadDocumentRequest request);
    Document getDocumentById(GetDocumentByIdRequest request);
    List<Document> getDocuments(GetDocumentsRequest request);
    List<Document> searchDocumentByName(SearchDocumentByNameRequest request);
    int getDocumentCount(UUID folderId);
    int getDocumentCountWithKeyword(UUID folderId, String keyword);
    void updateDocumentName(UpdateDocumentNameRequest request);
    void moveDocument(MoveDocumentRequest request);
    void deleteDocument(DeleteDocumentRequest request);
}
