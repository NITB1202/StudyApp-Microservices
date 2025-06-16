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
@Table(name = "team_usage")
public class TeamUsage {
    @Id
    @Column(name = "team_id", nullable = false)
    private UUID teamId;

    @Column(name = "used", nullable = false)
    private Long used;

    @Column(name = "total", nullable = false)
    private Long total;
}
