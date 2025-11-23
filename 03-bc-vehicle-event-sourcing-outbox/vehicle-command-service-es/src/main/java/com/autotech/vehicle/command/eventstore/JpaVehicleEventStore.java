package com.autotech.vehicle.command.eventstore;

import com.autotech.vehicle.command.domain.event.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class JpaVehicleEventStore implements VehicleEventStore {

    private final VehicleEventRepository eventRepository;
    private final VehicleOutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

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

                VehicleEventEntity entity = new VehicleEventEntity();
                entity.setAggregateId(aggregateId);
                entity.setAggregateType(aggregateType);
                entity.setVersion(version);
                entity.setEventType(eventType);
                entity.setPayload(payloadJson);
                entity.setMetadata(null);
                entity.setOccurredAt(Instant.now());
                eventRepository.save(entity);

                var envelopeNode = objectMapper.createObjectNode();
                envelopeNode.put("eventId", UUID.randomUUID().toString());
                envelopeNode.put("aggregateId", aggregateId);
                envelopeNode.put("aggregateType", aggregateType);
                envelopeNode.put("eventType", eventType);
                envelopeNode.put("occurredAt", Instant.now().toString());
                envelopeNode.set("payload", objectMapper.readTree(payloadJson));
                String envelopeJson = objectMapper.writeValueAsString(envelopeNode);

                VehicleOutboxEventEntity outbox = new VehicleOutboxEventEntity();
                outbox.setAggregateId(aggregateId);
                outbox.setAggregateType(aggregateType);
                outbox.setEventType(eventType);
                outbox.setTopic("vehicle-events");
                outbox.setPayload(envelopeJson);
                outbox.setHeaders(null);
                outbox.setStatus("PENDING");
                outbox.setCreatedAt(Instant.now());
                outboxRepository.save(outbox);

            } catch (Exception e) {
                log.error("Error while saving vehicle event", e);
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object> load(String aggregateId) {
        List<VehicleEventEntity> entities =
                eventRepository.findByAggregateIdOrderByVersionAsc(aggregateId);
        List<Object> events = new ArrayList<>();
        for (VehicleEventEntity entity : entities) {
            events.add(toDomainEvent(entity.getEventType(), entity.getPayload()));
        }
        return events;
    }

    private Object toDomainEvent(String eventType, String payloadJson) {
        try {
            return switch (eventType) {
                case "VehicleRegisteredEvent" ->
                        objectMapper.readValue(payloadJson, VehicleRegisteredEvent.class);
                case "VehicleDetailsUpdatedEvent" ->
                        objectMapper.readValue(payloadJson, VehicleDetailsUpdatedEvent.class);
                case "VehicleStatusChangedEvent" ->
                        objectMapper.readValue(payloadJson, VehicleStatusChangedEvent.class);
                case "VehicleOwnerAssignedEvent" ->
                        objectMapper.readValue(payloadJson, VehicleOwnerAssignedEvent.class);
                case "VehicleOwnerUnassignedEvent" ->
                        objectMapper.readValue(payloadJson, VehicleOwnerUnassignedEvent.class);
                default -> throw new IllegalArgumentException("Unknown event type " + eventType);
            };
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
