package com.study.authservice.service;

import com.study.authservice.grpc.VerificationType;

public interface CodeService {
    String generateCode(String email, VerificationType type);
    boolean verifyCode(String email, String code, VerificationType type);
}
