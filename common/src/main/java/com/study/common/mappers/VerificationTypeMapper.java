package com.study.common.mappers;

import com.study.common.enums.VerificationType;
import com.study.common.exceptions.BusinessException;

public class VerificationTypeMapper {
    private VerificationTypeMapper() {}

    public static VerificationType toEnum(com.study.authservice.grpc.VerificationType type) {
        return switch (type) {
            case REGISTER -> VerificationType.REGISTER;
            case RESET_PASSWORD -> VerificationType.RESET_PASSWORD;
            default -> throw new BusinessException("Invalid verification type");
        };
    }
}
