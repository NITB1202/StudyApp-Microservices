package com.study.planservice.event;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlanEventPublisher {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishEvent(String topic, Object event) {
        kafkaTemplate.send(topic,event);
    }
}
