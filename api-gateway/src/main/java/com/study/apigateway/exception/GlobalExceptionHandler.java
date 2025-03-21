package com.study.apigateway.exception;

import io.grpc.StatusRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ServerWebInputException;

import java.io.IOException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Unexpected error",
                e.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        StringBuilder errors = new StringBuilder();
        e.getBindingResult().getFieldErrors().forEach(error ->
                errors.append(String.format("[%s: %s] ", error.getField(), error.getDefaultMessage()))
        );

        ErrorResponse response = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation error",
                errors.toString()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = ServerWebInputException.class)
    public ResponseEntity<ErrorResponse> handleServerWebInputException(ServerWebInputException e) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Json parse error",
                e.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = IOException.class)
    public ResponseEntity<ErrorResponse> handleIOException(IOException e) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "I/O error",
                e.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = StatusRuntimeException.class)
    public ResponseEntity<ErrorResponse> handleStatusRuntimeException(StatusRuntimeException e) {
        int statusCode;
        HttpStatus httpStatus;

        switch (e.getStatus().getCode()) {
            case INVALID_ARGUMENT: {
                statusCode = HttpStatus.BAD_REQUEST.value();
                httpStatus = HttpStatus.BAD_REQUEST;
                break;
            }
            case NOT_FOUND: {
                statusCode = HttpStatus.NOT_FOUND.value();
                httpStatus = HttpStatus.NOT_FOUND;
                break;
            }
            default: {
                statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
                httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
                break;
            }
        }

        ErrorResponse response = new ErrorResponse(
                statusCode,
                "GRPC error",
                e.getMessage()
        );

        return new ResponseEntity<>(response, httpStatus);
    }
}
