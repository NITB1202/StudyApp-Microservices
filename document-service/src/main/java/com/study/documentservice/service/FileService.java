package com.study.documentservice.service;

import com.google.protobuf.ByteString;
import com.study.documentservice.dto.FileResponseDto;

public interface FileService {
    FileResponseDto uploadFile(String folderPath, String publicId, ByteString byteString);
    void moveFile(String publicId, String newFolder);
    void deleteFile(String publicId);
}
