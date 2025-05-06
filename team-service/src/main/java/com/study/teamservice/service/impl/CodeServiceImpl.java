package com.study.teamservice.service.impl;

import com.study.teamservice.service.CodeService;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class CodeServiceImpl implements CodeService {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int CODE_LENGTH = 5;
    private final SecureRandom random = new SecureRandom();

    @Override
    public String generateRandomCode() {
        StringBuilder sb = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }
}
