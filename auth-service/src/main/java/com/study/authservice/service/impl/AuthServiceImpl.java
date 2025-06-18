package com.study.authservice.service.impl;

import com.study.authservice.entity.Account;
import com.study.authservice.grpc.*;
import com.study.authservice.repository.AccountRepository;
import com.study.authservice.service.AuthService;
import com.study.authservice.service.CodeService;
import com.study.common.enums.Provider;
import com.study.common.enums.UserRole;
import com.study.common.exceptions.BusinessException;
import com.study.common.exceptions.NotFoundException;
import com.study.common.mappers.ProviderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final CodeService codeService;

    @Override
    public Account loginWithCredentials(LoginWithCredentialsRequest request) {
        Account account = accountRepository.findByEmail(request.getEmail());

        if(account == null) {
            throw new NotFoundException("Invalid email.");
        }

        if(!passwordEncoder.matches(request.getPassword(), account.getHashedPassword())) {
            throw new BusinessException("Incorrect password.");
        }

        return account;
    }

    @Override
    public Account loginWithProvider(LoginWithProviderRequest request) {
        String providerId = request.getProviderId();
        Provider provider = ProviderMapper.toProvider(request.getProvider());

        Account account = accountRepository.findByProviderIdAndProvider(providerId, provider);

        if(account == null) {
            throw new NotFoundException("Incorrect provider id or provider.");
        }

        return account;
    }

    @Override
    public boolean IsAccountRegistered(IsAccountRegisteredRequest request) {
        String accountId = request.getProviderId();
        Provider provider = ProviderMapper.toProvider(request.getProvider());

        return accountRepository.existsByProviderIdAndProvider(accountId, provider);
    }

    @Override
    public boolean validateEmail(String email) {
        return !accountRepository.existsByEmail(email);
    }

    @Override
    public boolean validatePassword(String password) {
        //Must be at least 3 characters long and contain both letters and numbers.
        String passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{3,}$";
        return password.matches(passwordRegex);
    }

    @Override
    public String getPasswordRule() {
        return "Password must be at least 3 characters long and contain both letters and numbers.";
    }

    @Override
    public void registerWithCredentials(RegisterWithCredentialsRequest request) {
        if(codeService.verifyCode(request.getEmail(), request.getVerificationCode(), VerificationType.REGISTER)){
            if(!validateEmail(request.getEmail())) {
                throw new BusinessException("This email has been used.");
            }

            if(!validatePassword(request.getPassword())) {
                throw new BusinessException(getPasswordRule());
            }

            UUID userId = UUID.fromString(request.getUserId());
            String hashedPassword = passwordEncoder.encode(request.getPassword());

            Account account = Account.builder()
                    .userId(userId)
                    .provider(Provider.LOCAL)
                    .email(request.getEmail())
                    .hashedPassword(hashedPassword)
                    .role(UserRole.USER)
                    .build();

            accountRepository.save(account);
        }
        else{
            throw new BusinessException("Verify failed.");
        }
    }

    @Override
    public void registerWithProvider(RegisterWithProviderRequest request) {
        UUID userId = UUID.fromString(request.getUserId());
        Provider provider = ProviderMapper.toProvider(request.getProvider());

        Account account = Account.builder()
                .userId(userId)
                .provider(provider)
                .providerId(request.getProviderId())
                .email(request.getEmail())
                .role(UserRole.USER)
                .build();

        accountRepository.save(account);
    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {
        if(codeService.verifyCode(request.getEmail(), request.getVerificationCode(), VerificationType.RESET_PASSWORD)){
            Account account = accountRepository.findByEmail(request.getEmail());

            if(account == null) {
                throw new NotFoundException("Account not found.");
            }

            if(!validatePassword(request.getNewPassword())) {
                throw new BusinessException(getPasswordRule());
            }

            String hashedPassword = passwordEncoder.encode(request.getNewPassword());
            account.setHashedPassword(hashedPassword);
            accountRepository.save(account);
        }
        else{
            throw new BusinessException("Reset password failed.");
        }
    }
}
