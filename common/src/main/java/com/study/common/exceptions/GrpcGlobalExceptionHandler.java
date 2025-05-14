package com.study.common.exceptions;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;

import java.time.format.DateTimeParseException;
import java.util.stream.Collectors;

@Slf4j
@GrpcAdvice
public class GrpcGlobalExceptionHandler {
    @GrpcExceptionHandler(Exception.class)
    public StatusRuntimeException handleDefaultException(Exception ex) {
        log.warn("Internal server error occurred: {}", ex.getMessage());
        return Status.INTERNAL.withDescription("Internal server error").asRuntimeException();
    }

    @GrpcExceptionHandler(BusinessException.class)
    public StatusRuntimeException handleBusinessException(BusinessException ex) {
        log.warn("Business exception occurred: {}", ex.getMessage());
        return Status.INVALID_ARGUMENT.withDescription(ex.getMessage()).asRuntimeException();
    }

    @GrpcExceptionHandler(NotFoundException.class)
    public StatusRuntimeException handleNotFoundException(NotFoundException ex) {
        log.warn("Resource not found: {}", ex.getMessage());
        return Status.NOT_FOUND.withDescription(ex.getMessage()).asRuntimeException();
    }

    @GrpcExceptionHandler(IllegalArgumentException.class)
    public StatusRuntimeException handleIllegalArgumentException(IllegalArgumentException ex) {
        log.warn("Invalid argument: {}", ex.getMessage());
        return Status.INVALID_ARGUMENT.withDescription(ex.getMessage()).asRuntimeException();
    }

    @GrpcExceptionHandler(ConstraintViolationException.class)
    public StatusRuntimeException handleConstraintViolationException(ConstraintViolationException ex) {
        String errors = ex.getConstraintViolations().stream()
                .map(cv -> String.format("[%s: %s]", cv.getPropertyPath(), cv.getMessage()))
                .collect(Collectors.joining(" "));
        log.warn("Constraint violation errors: {}", errors);
        return Status.INVALID_ARGUMENT.withDescription(errors).asRuntimeException();
    }

    @GrpcExceptionHandler(DateTimeParseException.class)
    public StatusRuntimeException handleDateTimeParseException(DateTimeParseException ex) {
        log.warn("DateTime parsing error: {}", ex.getMessage());
        return Status.INVALID_ARGUMENT.withDescription(ex.getMessage()).asRuntimeException();
    }
}
