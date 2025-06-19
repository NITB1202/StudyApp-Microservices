package com.study.authservice.service;

import com.study.authservice.entity.Account;
import com.study.authservice.grpc.GetAccountByIdRequest;

public interface AccountService {
    Account getAccountById(GetAccountByIdRequest request);
}
