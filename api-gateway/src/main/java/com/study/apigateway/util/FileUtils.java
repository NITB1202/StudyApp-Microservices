package com.study.apigateway.util;

import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;

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

}
