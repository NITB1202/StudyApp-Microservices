package com.study.documentservice.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.google.protobuf.ByteString;
import com.study.common.exceptions.BusinessException;
import com.study.documentservice.dto.FileResponseDto;
import com.study.documentservice.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final Cloudinary cloudinary;

    @Override
    public FileResponseDto uploadFile(String folderPath, String publicId, ByteString byteString) {
        byte[] bytes = byteString.toByteArray();

        Map params = ObjectUtils.asMap(
                "resource_type", "auto",
                "public_id", publicId,
                "asset_folder", folderPath,
                "overwrite", "true"
        );

        try {
            Map result = cloudinary.uploader().upload(bytes, params);
            String url = result.get("secure_url").toString();
            long fileSizeInBytes = ((Number) result.get("bytes")).longValue();

            return FileResponseDto.builder()
                    .url(url)
                    .bytes(fileSizeInBytes)
                    .build();
        }
        catch (IOException e) {
            throw new BusinessException("Error while uploading file.");
        }
    }

    @Override
    public void moveFile(String publicId, String newFolder) {
        try {
            cloudinary.api().update(publicId,ObjectUtils.asMap("asset_folder", newFolder));
        } catch (Exception e) {
            throw new BusinessException("Error while moving file.");
        }
    }

    @Override
    public void deleteFile(String publicId) {
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        }
        catch (IOException e) {
            throw new BusinessException("Error while deleting file.");
        }
    }
}
