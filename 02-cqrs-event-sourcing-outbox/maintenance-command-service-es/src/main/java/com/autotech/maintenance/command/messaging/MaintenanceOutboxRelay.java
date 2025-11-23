package com.autotech.maintenance.command.messaging;

import com.autotech.maintenance.command.constants.MaintenanceConstants;
import com.autotech.maintenance.command.eventstore.jpa.entity.MaintenanceOutboxEventEntity;
import com.autotech.maintenance.command.eventstore.jpa.reposiroty.MaintenanceOutboxRepository;
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
public class MaintenanceOutboxRelay {

    private final MaintenanceOutboxRepository outboxRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Scheduled(fixedDelay = 3000)
    @Transactional
    public void publishPendingEvents() {
        List<MaintenanceOutboxEventEntity> events =
                outboxRepository.findTop50ByStatusOrderByCreatedAtAsc(MaintenanceConstants.OUTBOX_EVENT_PENDING_STATUS);
        for (MaintenanceOutboxEventEntity outbox : events) {
            try {
                log.info("Publishing maintenance event id={} type={} topic={}",
                        outbox.getId(), outbox.getEventType(), outbox.getTopic());
                kafkaTemplate.send(outbox.getTopic(), outbox.getAggregateId(), outbox.getPayload()).get();
                outbox.setStatus(MaintenanceConstants.OUTBOX_EVENT_SENT_STATUS);
                outbox.setLastAttemptAt(Instant.now());
                outbox.setErrorMessage(null);
            } catch (Exception e) {
                log.error("Error while publishing outbox event id={}", outbox.getId(), e);
                outbox.setStatus("FAILED");
                outbox.setLastAttemptAt(Instant.now());
                outbox.setErrorMessage(e.getMessage());
            }
        }
    }
}
