package com.study.documentservice.service;

import com.google.protobuf.ByteString;

public interface FileService {
    String uploadFile(String folderPath, String publicId, ByteString byteString);
    void moveFile(String publicId, String newFolder);
    void deleteFile(String publicId);
}
