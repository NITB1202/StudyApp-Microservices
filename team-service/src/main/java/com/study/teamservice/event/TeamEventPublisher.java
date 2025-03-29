package com.study.teamservice.event;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TeamEventPublisher {
    private static final String TOPIC = "team-updated";
    private final KafkaTemplate<String, TeamUpdatedEvent> kafkaTemplate;

    public void publishTeamUpdatedEvent(TeamUpdatedEvent event) {
        kafkaTemplate.send(TOPIC, event);
    }
}