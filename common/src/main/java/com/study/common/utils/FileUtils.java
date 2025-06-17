package com.study.common.utils;

import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.multipart.MultipartFile;

public class FileUtils {

    public static boolean isDocument(FilePart filePart) {
        String filename = filePart.filename().toLowerCase();
        return filename.endsWith(".pdf") ||
                filename.endsWith(".doc") ||
                filename.endsWith(".docx") ||
                filename.endsWith(".xls") ||
                filename.endsWith(".xlsx") ||
                filename.endsWith(".ppt") ||
                filename.endsWith(".pptx") ||
                filename.endsWith(".txt");
    }

    public static boolean isImage(FilePart file) {
        MediaType mediaType = file.headers().getContentType();

        String contentType = mediaType != null
                ? mediaType.toString()
                : "";

        return contentType.startsWith("image/");
    }

    public static boolean isImage(MultipartFile file) {
        if (file == null || file.isEmpty()) return false;

        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }

}
