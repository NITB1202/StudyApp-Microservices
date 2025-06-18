package com.study.authservice.repository;

import com.study.authservice.entity.Account;
import com.study.common.enums.Provider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    boolean existsByProviderIdAndProvider(String providerId, Provider provider);
    boolean existsByEmail(String email);
    Account findByEmail(String email);
    Account findByProviderIdAndProvider(String providerId, Provider provider);
}