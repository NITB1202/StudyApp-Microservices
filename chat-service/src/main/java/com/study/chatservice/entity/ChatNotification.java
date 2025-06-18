package com.study.chatservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "chat_notifications")
public class ChatNotification {
    @Id
    @Column(name = "team_id", nullable = false)
    private UUID teamId;

    @Column(name = "team_name", nullable = false)
    private String teamName;

    @Column(name = "team_avatar_url")
    private String teamAvatarUrl;

    @Column(name = "new_message_count", nullable = false)
    private int newMessageCount;
}
