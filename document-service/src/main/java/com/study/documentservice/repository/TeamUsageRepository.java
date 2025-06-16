package com.study.documentservice.repository;

import com.study.documentservice.entity.TeamUsage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TeamUsageRepository extends JpaRepository<TeamUsage, UUID> {
}