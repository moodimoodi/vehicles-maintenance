package com.autotech.vehicle.command.messaging;

import com.autotech.vehicle.command.eventstore.jpa.entity.VehicleOutboxEventEntity;
import com.autotech.vehicle.command.eventstore.jpa.repository.VehicleOutboxRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests du composant VehicleOutboxRelay.
 *
 * Objectif :
 *  - S'assurer que publishPendingEvents():
 *      * lit bien les événements PENDING dans l'outbox
 *      * envoie le message sur Kafka
 *      * met à jour le statut (SENT / FAILED), lastAttemptAt, errorMessage
 */
class VehicleOutboxRelayTest {

    private VehicleOutboxRepository outboxRepository;
    private KafkaTemplate<String, String> kafkaTemplate;

    private VehicleOutboxRelay relay;

    @BeforeEach
    void setUp() {
        outboxRepository = mock(VehicleOutboxRepository.class);
        kafkaTemplate = mock(KafkaTemplate.class);

        relay = new VehicleOutboxRelay(outboxRepository, kafkaTemplate);
    }

    @Test
    void publishPendingEvents_shouldSendToKafkaAndMarkAsSent() throws Exception {
        // GIVEN
        VehicleOutboxEventEntity pending = new VehicleOutboxEventEntity();
        pending.setId(1L);
        pending.setAggregateId("VIN-123");
        pending.setAggregateType("Vehicle");
        pending.setEventType("VehicleRegisteredEvent");
        pending.setTopic("vehicle-events");
        pending.setPayload("{\"foo\":\"bar\"}");
        pending.setStatus("PENDING");
        pending.setCreatedAt(Instant.now());

        when(outboxRepository.findTop50ByStatusOrderByCreatedAtAsc("PENDING"))
                .thenReturn(List.of(pending));

        // On simule un send() Kafka qui réussit immédiatement
        CompletableFuture<SendResult<String, String>> future = new CompletableFuture<>();
        future.complete(new SendResult<>(null, null));
        when(kafkaTemplate.send("vehicle-events", "VIN-123", "{\"foo\":\"bar\"}"))
                .thenReturn(future);

        // WHEN
        relay.publishPendingEvents();

        // THEN
        // Le statut doit passer à SENT et lastAttemptAt doit être renseigné
        assertEquals("SENT", pending.getStatus());
        assertNotNull(pending.getLastAttemptAt());
        assertNull(pending.getErrorMessage());

        // On vérifie que kafkaTemplate.send a bien été appelé
        verify(kafkaTemplate).send("vehicle-events", "VIN-123", "{\"foo\":\"bar\"}");
    }

    @Test
    void publishPendingEvents_shouldMarkAsFailedWhenKafkaThrows() throws Exception {
        // GIVEN
        VehicleOutboxEventEntity pending = new VehicleOutboxEventEntity();
        pending.setId(1L);
        pending.setAggregateId("VIN-123");
        pending.setAggregateType("Vehicle");
        pending.setEventType("VehicleRegisteredEvent");
        pending.setTopic("vehicle-events");
        pending.setPayload("{\"foo\":\"bar\"}");
        pending.setStatus("PENDING");
        pending.setCreatedAt(Instant.now());

        when(outboxRepository.findTop50ByStatusOrderByCreatedAtAsc("PENDING"))
                .thenReturn(List.of(pending));

        // Cette fois-ci, on simule une erreur de Kafka
        CompletableFuture<SendResult<String, String>> future = new CompletableFuture<>();
        future.completeExceptionally(new RuntimeException("Kafka down"));
        when(kafkaTemplate.send("vehicle-events", "VIN-123", "{\"foo\":\"bar\"}"))
                .thenReturn(future);

        // WHEN
        relay.publishPendingEvents();

        // THEN
        // Le statut doit passer à FAILED et errorMessage doit être renseigné
        assertEquals("FAILED", pending.getStatus());
        assertNotNull(pending.getLastAttemptAt());
        assertNotNull(pending.getErrorMessage());
        assertTrue(pending.getErrorMessage().contains("Kafka down"));
    }
}
