package com.study.authservice.service;

public interface MailService {
    void sendVerificationEmail(String email, String verificationCode);
}
