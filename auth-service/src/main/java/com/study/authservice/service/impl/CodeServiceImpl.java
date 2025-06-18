package com.study.authservice.service.impl;

import com.study.authservice.grpc.VerificationType;
import com.study.authservice.service.CodeService;
import com.study.common.mappers.VerificationTypeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class CodeServiceImpl implements CodeService {
    private final RedisTemplate<String, Object> redisTemplate;
    private static final int LENGTH = 6;
    private static final int MINUTES = 5;

    @Override
    public String generateCode(String email, VerificationType type) {
        String code = generateRandomCode();
        String key = generateKey(email, type);

        redisTemplate.opsForValue().set(key, code, Duration.ofMinutes(MINUTES));

        return code;
    }

    @Override
    public boolean verifyCode(String email, String code, VerificationType type) {
        String key = generateKey(email, type);
        String storedCode = (String) redisTemplate.opsForValue().get(key);

        if(storedCode == null || !storedCode.equals(code)) {
            return false;
        }

        redisTemplate.delete(key);
        return true;
    }

    private String generateKey(String email, VerificationType type) {
        com.study.common.enums.VerificationType enumType = VerificationTypeMapper.toEnum(type);
        return enumType + ":" + email.toLowerCase();
    }

    private String generateRandomCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < LENGTH; i++) {
            int rand = (int) (Math.random() * chars.length());
            sb.append(chars.charAt(rand));
        }
        return sb.toString();
    }
}
