package com.autotech.vehicle.command.messaging;

import com.autotech.vehicle.command.eventstore.jpa.entity.VehicleOutboxEventEntity;
import com.autotech.vehicle.command.eventstore.jpa.repository.VehicleOutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class VehicleOutboxRelay {

    private final VehicleOutboxRepository outboxRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Scheduled(fixedDelay = 3000)
    @Transactional
    public void publishPendingEvents() {
        List<VehicleOutboxEventEntity> events =
                outboxRepository.findTop50ByStatusOrderByCreatedAtAsc("PENDING");
        for (VehicleOutboxEventEntity outbox : events) {
            try {
                log.info("Publishing vehicle event id={} type={} topic={}",
                        outbox.getId(), outbox.getEventType(), outbox.getTopic());
                kafkaTemplate.send(outbox.getTopic(), outbox.getAggregateId(), outbox.getPayload()).get();
                outbox.setStatus("SENT");
                outbox.setLastAttemptAt(Instant.now());
                outbox.setErrorMessage(null);
            } catch (Exception e) {
                log.error("Error while publishing vehicle outbox event id={}", outbox.getId(), e);
                outbox.setStatus("FAILED");
                outbox.setLastAttemptAt(Instant.now());
                outbox.setErrorMessage(e.getMessage());
            }
        }
    }
}
