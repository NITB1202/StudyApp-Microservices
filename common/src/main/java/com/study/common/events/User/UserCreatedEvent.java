package com.study.common.events.User;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreatedEvent {
    private UUID userId;
}
