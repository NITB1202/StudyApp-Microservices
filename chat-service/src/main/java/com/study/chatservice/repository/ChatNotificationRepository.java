package com.study.chatservice.repository;

import com.study.chatservice.entity.ChatNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ChatNotificationRepository extends JpaRepository<ChatNotification, UUID> {
}