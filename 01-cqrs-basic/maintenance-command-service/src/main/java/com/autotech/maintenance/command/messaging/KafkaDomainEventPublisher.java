package com.autotech.maintenance.command.messaging;

import com.autotech.maintenance.core.messaging.DomainEventEnvelope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaDomainEventPublisher implements DomainEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaDomainEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public <T> void publish(String topic, DomainEventEnvelope<T> envelope) {
        String key = envelope.getAggregateId();

        log.info("Publishing event to Kafka: topic={}, key={}, type={}",
                topic, key, envelope.getEventType());

        kafkaTemplate.send(topic, key, envelope)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to publish event to Kafka", ex);
                    } else if (result != null && result.getRecordMetadata() != null) {
                        log.info("Event published to topic={}, partition={}, offset={}",
                                result.getRecordMetadata().topic(),
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                    }
                });
    }
}
