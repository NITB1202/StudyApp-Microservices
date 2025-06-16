package com.study.apigateway.util;

import org.springframework.http.codec.multipart.FilePart;

public class FileUtil {

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

}
