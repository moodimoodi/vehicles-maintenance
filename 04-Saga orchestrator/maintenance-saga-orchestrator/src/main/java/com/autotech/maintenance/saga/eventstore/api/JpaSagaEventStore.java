package com.autotech.maintenance.saga.eventstore.api;

import com.autotech.maintenance.saga.constants.SagaConstants;
import com.autotech.maintenance.saga.domain.aggregate.MaintenanceBookingSagaAggregate;
import com.autotech.maintenance.saga.domain.saga.events.*;
import com.autotech.maintenance.saga.eventstore.entity.SagaEventEntity;
import com.autotech.maintenance.saga.eventstore.entity.SagaOutboxEventEntity;
import com.autotech.maintenance.saga.eventstore.repository.SagaEventRepository;
import com.autotech.maintenance.saga.eventstore.repository.SagaOutboxRepository;
import com.autotech.maintenance.saga.messaging.commands.VehicleReserveCommandMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JpaSagaEventStore implements SagaEventStore {

    private final SagaEventRepository eventRepository;
    private final SagaOutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public void save(MaintenanceBookingSagaAggregate saga, String sagaType, int expectedVersion, List<Object> events) {
        int version = expectedVersion;
        for (Object event : events) {
            version++;
            String eventType = event.getClass().getSimpleName();
            try {

                // Saga event store save
                String payloadJson = objectMapper.writeValueAsString(event);
                SagaEventEntity entity = new SagaEventEntity();
                entity.setSagaId(saga.getSagaId());
                entity.setSagaType(sagaType);
                entity.setVersion(version);
                entity.setEventType(eventType);
                entity.setPayload(payloadJson);
                entity.setOccurredAt(Instant.now());
                eventRepository.save(entity);

                // Payload construct
                VehicleReserveCommandMessage cmd = new VehicleReserveCommandMessage(

                        saga.getSagaId(),
                        saga.getVehicleVin(),
                        saga.getCustomerId()
                );
                String payloadJsonOutbox = objectMapper.writeValueAsString(cmd);

                // Saga event outbox save
                SagaOutboxEventEntity outbox = new SagaOutboxEventEntity();
                outbox.setSagaId(saga.getSagaId());
                outbox.setSagaType(SagaConstants.OUTBOX_SAGA_BOOKING_MAINTENANCE_EVENT);
                outbox.setTopic(SagaConstants.KAFKA_TOPIC_SAGA_VEHICLE_EVENTS_NAME);
                outbox.setKey_id(saga.getVehicleVin());
                outbox.setPayload(payloadJsonOutbox);
                outbox.setStatus(SagaConstants.OUTBOX_SAGA_EVENT_PENDING_STATUS);
                outbox.setCreatedAt(Instant.now());
                outboxRepository.save(outbox);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object> load(String sagaId) {
        List<SagaEventEntity> entities = eventRepository.findBySagaIdOrderByVersionAsc(sagaId);
        List<Object> events = new ArrayList<>();
        for (SagaEventEntity entity : entities) {
            events.add(toDomainEvent(entity.getEventType(), entity.getPayload()));
        }
        return events;
    }

    private Object toDomainEvent(String eventType, String payloadJson) {
        try {
            return switch (eventType) {
                case "SagaStartedEvent" -> objectMapper.readValue(payloadJson, SagaStartedEvent.class);
                case "VehicleReservedEvent" -> objectMapper.readValue(payloadJson, VehicleReservedEvent.class);
                case "VehicleReservationFailedEvent" -> objectMapper.readValue(payloadJson, VehicleReservationFailedEvent.class);
                case "MaintenanceScheduledEvent" -> objectMapper.readValue(payloadJson, MaintenanceScheduledEvent.class);
                case "MaintenanceScheduleFailedEvent" -> objectMapper.readValue(payloadJson, MaintenanceScheduleFailedEvent.class);
                case "SagaCompletedEvent" -> objectMapper.readValue(payloadJson, SagaCompletedEvent.class);
                case "SagaCompensatedEvent" -> objectMapper.readValue(payloadJson, SagaCompensatedEvent.class);
                default -> throw new IllegalArgumentException("Unknown saga event type " + eventType);
            };
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
