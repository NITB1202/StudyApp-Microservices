package com.nitb.planservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "plan_id", nullable = false)
    private UUID planId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "assignee_id", nullable = false)
    private UUID assigneeId;

    @Column(name = "is_completed", nullable = false)
    private Boolean isCompleted;
}
