package com.autotech.vehicle.command.messaging;

import com.autotech.maintenance.core.messaging.DomainEventEnvelope;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaDomainEventPublisher implements DomainEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public KafkaDomainEventPublisher(KafkaTemplate<String, Object> kafkaTemplate,
                                     ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public <T> void publish(String topic, DomainEventEnvelope<T> envelope) {
        try {
            String key = envelope.getAggregateId();
            log.info("Publishing vehicle event to topic={} key={} envelope={}", topic, key, envelope);
            kafkaTemplate.send(topic, key, envelope)
                         .whenComplete((result, ex) -> {
                             if (ex != null) {
                                 log.error("Error while sending event to topic={} key={}", topic, key, ex);
                             } else {
                                 log.info("Event sent successfully to topic={} partition={} offset={}",
                                         result.getRecordMetadata().topic(),
                                         result.getRecordMetadata().partition(),
                                         result.getRecordMetadata().offset());
                             }
                         });
        } catch (Exception e) {
            log.error("Failed to publish vehicle event to topic={}", topic, e);
            throw new RuntimeException("Failed to publish event", e);
        }
    }
}
