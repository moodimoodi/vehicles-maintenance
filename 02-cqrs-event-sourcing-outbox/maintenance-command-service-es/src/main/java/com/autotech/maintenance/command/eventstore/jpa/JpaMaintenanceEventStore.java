package com.autotech.maintenance.command.eventstore.jpa;

import com.autotech.maintenance.command.constants.MaintenanceConstants;
import com.autotech.maintenance.command.domain.events.*;
import com.autotech.maintenance.command.eventstore.api.MaintenanceEventStore;
import com.autotech.maintenance.command.eventstore.jpa.entity.MaintenanceEventEntity;
import com.autotech.maintenance.command.eventstore.jpa.entity.MaintenanceOutboxEventEntity;
import com.autotech.maintenance.command.eventstore.jpa.reposiroty.MaintenanceEventRepository;
import com.autotech.maintenance.command.eventstore.jpa.reposiroty.MaintenanceOutboxRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Classe to write Event maintenance store and outbox event
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JpaMaintenanceEventStore implements MaintenanceEventStore {

    private final MaintenanceEventRepository eventRepository;
    private final MaintenanceOutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    /**
     * Write Event maintenance store and outbox event
     * @param aggregateId
     * @param aggregateType
     * @param expectedVersion
     * @param events
     */
    @Override
    @Transactional
    public void save(String aggregateId,
                     String aggregateType,
                     int expectedVersion,
                     List<Object> events) {

        int version = expectedVersion;
        for (Object event : events) {
            version++;
            String eventType = event.getClass().getSimpleName();
            try {
                String payloadJson = objectMapper.writeValueAsString(event);

                // Save maintenance event entity
                MaintenanceEventEntity entity = new MaintenanceEventEntity();
                entity.setAggregateId(aggregateId);
                entity.setAggregateType(aggregateType);
                entity.setVersion(version);
                entity.setEventType(eventType);
                entity.setPayload(payloadJson);
                entity.setMetadata(null);
                entity.setOccurredAt(Instant.now());
                eventRepository.save(entity);

                // Construct payload of outbox event
                var envelopeNode = objectMapper.createObjectNode();
                envelopeNode.put("eventId", UUID.randomUUID().toString());
                envelopeNode.put("aggregateId", aggregateId);
                envelopeNode.put("aggregateType", aggregateType);
                envelopeNode.put("eventType", eventType);
                envelopeNode.put("occurredAt", Instant.now().toString());
                envelopeNode.set("payload", objectMapper.readTree(payloadJson));
                String envelopeJson = objectMapper.writeValueAsString(envelopeNode);

                // save outbox event
                MaintenanceOutboxEventEntity outbox = new MaintenanceOutboxEventEntity();
                outbox.setAggregateId(aggregateId);
                outbox.setAggregateType(aggregateType);
                outbox.setEventType(eventType);
                outbox.setTopic(MaintenanceConstants.KAFKA_TOPIC_MAINTENANCE_EVENTS_NAME);
                outbox.setPayload(envelopeJson);
                outbox.setHeaders(null);
                outbox.setStatus(MaintenanceConstants.OUTBOX_EVENT_PENDING_STATUS);
                outbox.setCreatedAt(Instant.now());
                outboxRepository.save(outbox);

            } catch (Exception e) {
                log.error("Error while saving maintenance event", e);
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object> load(String aggregateId) {
        List<MaintenanceEventEntity> entities =
                eventRepository.findByAggregateIdOrderByVersionAsc(aggregateId);
        List<Object> events = new ArrayList<>();
        for (MaintenanceEventEntity entity : entities) {
            events.add(toDomainEvent(entity.getEventType(), entity.getPayload()));
        }
        return events;
    }

    private Object toDomainEvent(String eventType, String payloadJson) {
        try {
            return switch (eventType) {
                case "MaintenanceScheduledEvent" ->
                        objectMapper.readValue(payloadJson, MaintenanceScheduledEvent.class);
                case "MaintenanceRescheduledEvent" ->
                        objectMapper.readValue(payloadJson, MaintenanceRescheduledEvent.class);
                case "MaintenanceStatusChangedEvent" ->
                        objectMapper.readValue(payloadJson, MaintenanceStatusChangedEvent.class);
                case "MaintenanceCancelledEvent" ->
                        objectMapper.readValue(payloadJson, MaintenanceCancelledEvent.class);
                case "MaintenanceCompletedEvent" ->
                        objectMapper.readValue(payloadJson, MaintenanceCompletedEvent.class);
                default -> throw new IllegalArgumentException("Unknown event type " + eventType);
            };
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
