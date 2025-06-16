package com.study.documentservice.repository;

import com.study.documentservice.entity.Folder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FolderRepository extends JpaRepository<Folder, UUID> {
}