package com.study.documentservice.repository;

import com.study.documentservice.entity.UserUsage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserUsageRepository extends JpaRepository<UserUsage, UUID> {
}