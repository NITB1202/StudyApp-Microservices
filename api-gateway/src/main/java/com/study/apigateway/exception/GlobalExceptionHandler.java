package com.study.apigateway.exception;

import io.grpc.StatusRuntimeException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

import java.io.IOException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public Mono<ResponseEntity<ErrorResponse>> handleException(Exception e) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Unexpected error",
                e.getMessage()
        );

        return Mono.just(new ResponseEntity<>(response, HttpStatus.BAD_REQUEST));
    }

   @ExceptionHandler(WebExchangeBindException.class)
   public Mono<ResponseEntity<ErrorResponse>> handleException(WebExchangeBindException e) {
       String errorMessage = e.getFieldErrors()
               .stream()
               .findFirst()
               .map(DefaultMessageSourceResolvable::getDefaultMessage)
               .orElse("Validation error");

       ErrorResponse errorResponse = new ErrorResponse(
               HttpStatus.BAD_REQUEST.value(),
               "Validation error",
               errorMessage
       );

       return Mono.just(ResponseEntity.badRequest().body(errorResponse));
   }

    @ExceptionHandler(value = ServerWebInputException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleServerWebInputException(ServerWebInputException e) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Json parse error",
                e.getMessage()
        );
        return Mono.just(new ResponseEntity<>(response, HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(value = IOException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleIOException(IOException e) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "I/O error",
                e.getMessage()
        );
        return Mono.just(new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler(value = StatusRuntimeException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleStatusRuntimeException(StatusRuntimeException e) {
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

        return Mono.just(new ResponseEntity<>(response, httpStatus));
    }
}
