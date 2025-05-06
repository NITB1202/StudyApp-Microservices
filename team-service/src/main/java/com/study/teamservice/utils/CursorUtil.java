package com.study.teamservice.utils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.UUID;

public class CursorUtil {
    private static final String SEPARATOR = "|";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

    public static String encodeCursor(LocalDate Date, UUID id) {
        String raw = formatter.format(Date) + SEPARATOR + id.toString();
        return Base64.getUrlEncoder().encodeToString(raw.getBytes(StandardCharsets.UTF_8));
    }

    public static DecodedCursor decodeCursor(String encodedCursor) {
        try {
            byte[] decodedBytes = Base64.getUrlDecoder().decode(encodedCursor);
            String raw = new String(decodedBytes, StandardCharsets.UTF_8);
            String[] parts = raw.split("\\" + SEPARATOR, 2);

            if (parts.length != 2) {
                throw new IllegalArgumentException("Invalid cursor format");
            }

            LocalDate date = LocalDate.parse(parts[0], formatter);
            UUID id = UUID.fromString(parts[1]);

            return new DecodedCursor(date, id);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to decode cursor", e);
        }
    }
}