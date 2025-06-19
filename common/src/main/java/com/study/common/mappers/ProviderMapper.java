package com.study.common.mappers;

import com.study.authservice.grpc.SupportedProvider;
import com.study.common.enums.Provider;
import com.study.common.exceptions.BusinessException;

public class ProviderMapper {
    private ProviderMapper() {}

    public static Provider toProvider(SupportedProvider supportedProvider) {
        return switch (supportedProvider) {
            case GOOGLE -> Provider.GOOGLE;
            default -> throw new BusinessException("Unsupported provider: " + supportedProvider);
        };
    }

    public static SupportedProvider toSupportedProvider(Provider provider) {
        return switch (provider) {
            case GOOGLE -> SupportedProvider.GOOGLE;
            default -> throw new BusinessException("Unsupported provider: " + provider);
        };
    }
}
