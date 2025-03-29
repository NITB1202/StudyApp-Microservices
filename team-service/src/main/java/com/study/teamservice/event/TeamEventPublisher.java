package com.study.teamservice.event;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TeamEventPublisher {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishEvent(String topic, Object event) {
        kafkaTemplate.send(topic,event);
    }
}