package com.autotech.maintenance.saga.messaging;

import com.autotech.maintenance.saga.constants.SagaConstants;
import com.autotech.maintenance.saga.eventstore.entity.SagaOutboxEventEntity;
import com.autotech.maintenance.saga.eventstore.repository.SagaOutboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SagaOutboxRelay {

    private final SagaOutboxRepository outboxRepository;
    private final KafkaTemplate<String, String> sagaKafkaTemplate;

    @Scheduled(fixedDelay = 3000)
    @Transactional
    public void publishPendingCommands() {
        List<SagaOutboxEventEntity> pending =
                outboxRepository.findTop50ByStatusOrderByCreatedAtAsc(SagaConstants.OUTBOX_SAGA_EVENT_PENDING_STATUS);
        for (SagaOutboxEventEntity outbox : pending) {
            try {
                sagaKafkaTemplate.send(outbox.getTopic(), outbox.getKey_id(), outbox.getPayload()).get();
                outbox.setStatus(SagaConstants.OUTBOX_SAGA_EVENT_SENT_STATUS);
                outbox.setLastAttemptAt(Instant.now());
                outbox.setErrorMessage(null);
            } catch (Exception e) {
                outbox.setStatus(SagaConstants.OUTBOX_SAGA_EVENT_FAILED_STATUS);
                outbox.setLastAttemptAt(Instant.now());
                outbox.setErrorMessage(e.getMessage());
            }
        }
    }
}
