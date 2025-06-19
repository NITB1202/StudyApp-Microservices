package com.study.authservice.mapper;

import com.study.authservice.entity.Account;
import com.study.authservice.grpc.AccountSummaryResponse;

public class AccountMapper {
    private AccountMapper() {}

    public static AccountSummaryResponse toAccountSummaryResponse(Account account) {
        return AccountSummaryResponse.newBuilder()
                .setId(account.getId().toString())
                .setUserId(account.getUserId().toString())
                .setRole(account.getRole().toString())
                .build();
    }
}
