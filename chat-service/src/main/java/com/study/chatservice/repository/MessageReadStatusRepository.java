package com.study.chatservice.repository;

import com.study.chatservice.entity.MessageReadStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MessageReadStatusRepository extends JpaRepository<MessageReadStatus, UUID> {
}