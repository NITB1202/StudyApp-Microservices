package com.study.common.service;

import com.google.protobuf.ByteString;
import com.study.common.dto.FileResponseDto;

public interface FileService {
    FileResponseDto uploadFile(String folderPath, String publicId, ByteString byteString);
    void moveFile(String publicId, String newFolder);
    void deleteFile(String publicId);
}
