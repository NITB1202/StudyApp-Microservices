package com.study.userservice.exceptions;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class ErrorResponse {
    private int statusCode;
    private String type;
    private String message;
    private String timestamp;

    public ErrorResponse(int statusCode, String type, String message) {
        this.statusCode = statusCode;
        this.type = type;
        this.message = message;
        this.timestamp = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }
}
