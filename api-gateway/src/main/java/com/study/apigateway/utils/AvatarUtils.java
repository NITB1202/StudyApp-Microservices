package com.study.apigateway.utils;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.study.common.exceptions.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AvatarUtils {
    private final Cloudinary cloudinary;

    public String uploadAvatar(UUID id, FilePart avatar) throws IOException {
        byte[] bytes = extractBytesBlocking(avatar);

        String fileType = avatar.headers().getContentType() != null
                ? avatar.headers().getContentType().toString()
                : null;

        if (fileType == null || !fileType.startsWith("image/")) {
            throw new BusinessException("Avatar is not an image");
        }

        //Upload the avatar to the Cloudinary
        Map params = ObjectUtils.asMap(
                "resource_type", "auto",
                "asset_folder", "avatars",
                "public_id", id.toString(),
                "overwrite", true
        );

        Map result = cloudinary.uploader().upload(bytes,params);

        return result.get("secure_url").toString();
    }

    private byte[] extractBytesBlocking(FilePart filePart) {
        // Collect all DataBuffer into single unit
        return DataBufferUtils.join(filePart.content())
                .map(dataBuffer -> {
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    DataBufferUtils.release(dataBuffer);
                    return bytes;
                })
                // Block and get result
                .block();
    }
}
