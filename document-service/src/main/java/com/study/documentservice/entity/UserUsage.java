package com.study.documentservice.entity;

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
@Table(name = "user_usage")
public class UserUsage {
    @Id
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "used", nullable = false)
    private Long used;

    @Column(name = "total", nullable = false)
    private Long total;
}
