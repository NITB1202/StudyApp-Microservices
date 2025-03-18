package com.study.userservice.enity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "dob", nullable = false)
    private LocalDate dateOfBirth;

//    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private String gender;

    @Column(name = "avatar_url")
    private String avatarUrl;
}