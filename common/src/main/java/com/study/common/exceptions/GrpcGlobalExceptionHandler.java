package com.study.common.exceptions;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;

import java.io.IOException;
import java.util.stream.Collectors;

@Slf4j
@GrpcAdvice
public class GrpcGlobalExceptionHandler {
    @GrpcExceptionHandler(Exception.class)
    public StatusRuntimeException handleDefaultException(Exception ex) {
        return Status.INTERNAL.withDescription("Internal server error").asRuntimeException();
    }

    @GrpcExceptionHandler(BusinessException.class)
    public StatusRuntimeException handleBusinessException(BusinessException ex) {
        return Status.INVALID_ARGUMENT.withDescription(ex.getMessage()).asRuntimeException();
    }

    @GrpcExceptionHandler(NotFoundException.class)
    public StatusRuntimeException handleNotFoundException(NotFoundException ex) {
        return Status.NOT_FOUND.withDescription(ex.getMessage()).asRuntimeException();
    }

    @GrpcExceptionHandler(IllegalArgumentException.class)
    public StatusRuntimeException handleIllegalArgumentException(IllegalArgumentException ex) {
        return Status.INVALID_ARGUMENT.withDescription(ex.getMessage()).asRuntimeException();
    }

    @GrpcExceptionHandler(ConstraintViolationException.class)
    public StatusRuntimeException handleConstraintViolationException(ConstraintViolationException ex) {
        String errors = ex.getConstraintViolations().stream()
                .map(cv -> String.format("[%s: %s]", cv.getPropertyPath(), cv.getMessage()))
                .collect(Collectors.joining(" "));

        return Status.INVALID_ARGUMENT.withDescription(errors).asRuntimeException();
    }

    @GrpcExceptionHandler(IOException.class)
    public StatusRuntimeException handleIOException(IOException ex) {
        return Status.UNAVAILABLE.withDescription("I/O error").asRuntimeException();
    }
}
