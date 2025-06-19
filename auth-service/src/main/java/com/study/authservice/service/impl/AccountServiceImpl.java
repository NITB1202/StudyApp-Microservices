package com.study.authservice.service.impl;

import com.study.authservice.entity.Account;
import com.study.authservice.grpc.GetAccountByIdRequest;
import com.study.authservice.repository.AccountRepository;
import com.study.authservice.service.AccountService;
import com.study.common.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    @Override
    public Account getAccountById(GetAccountByIdRequest request) {
        UUID id = UUID.fromString(request.getId());
        return accountRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Account not found.")
        );
    }
}
