package com.autotech.maintenance.saga.messaging;

import com.autotech.maintenance.saga.constants.SagaConstants;
import com.autotech.maintenance.saga.domain.aggregate.MaintenanceBookingSagaAggregate;
import com.autotech.maintenance.saga.domain.saga.repository.MaintenanceBookingSagaRepository;
import com.autotech.maintenance.saga.eventstore.entity.SagaOutboxEventEntity;
import com.autotech.maintenance.saga.eventstore.repository.SagaOutboxRepository;
import com.autotech.maintenance.saga.messaging.commands.MaintenanceScheduleCommandMessage;
import com.autotech.maintenance.saga.messaging.replies.MaintenanceScheduleReplyMessage;
import com.autotech.maintenance.saga.messaging.replies.VehicleReserveReplyMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class SagaReplyListener {

    private final MaintenanceBookingSagaRepository sagaRepository;
    private final SagaOutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "vehicle-replies", groupId = "maintenance-saga-orchestrator")
    @Transactional
    public void onVehicleReply(String rawJson) {
        try {
            VehicleReserveReplyMessage reply =
                    objectMapper.readValue(rawJson, VehicleReserveReplyMessage.class);
            MaintenanceBookingSagaAggregate saga = sagaRepository.load(reply.sagaId());

            if (reply.success()) {
                saga.onVehicleReserved();
                sagaRepository.save(saga);

                // Schedule maintenance for the reserved vehicle
                MaintenanceScheduleCommandMessage cmd = new MaintenanceScheduleCommandMessage(
                        saga.getSagaId(),
                        saga.getAppointmentId(),
                        saga.getVehicleVin(),
                        saga.getCustomerId(),
                        saga.getWorkshopId(),
                        saga.getMaintenanceType()
                );
                String payloadJson = objectMapper.writeValueAsString(cmd);

                SagaOutboxEventEntity outbox = new SagaOutboxEventEntity();
                outbox.setSagaId(saga.getSagaId());
                outbox.setSagaType(SagaConstants.OUTBOX_SAGA_BOOKING_MAINTENANCE_EVENT);
                outbox.setTopic("maintenance-commands");
                outbox.setKey_id(saga.getAppointmentId());
                outbox.setPayload(payloadJson);
                outbox.setStatus(SagaConstants.OUTBOX_SAGA_EVENT_PENDING_STATUS);
                outbox.setCreatedAt(Instant.now());
                outboxRepository.save(outbox);
            } else {
                saga.onVehicleReservationFailed(reply.reason());
                sagaRepository.save(saga);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @KafkaListener(topics = "maintenance-replies", groupId = "maintenance-saga-orchestrator")
    @Transactional
    public void onMaintenanceReply(String rawJson) {
        try {
            MaintenanceScheduleReplyMessage reply =
                    objectMapper.readValue(rawJson, MaintenanceScheduleReplyMessage.class);
            MaintenanceBookingSagaAggregate saga = sagaRepository.load(reply.sagaId());

            if (reply.success()) {
                saga.onMaintenanceScheduled();
            } else {
                saga.onMaintenanceScheduleFailed(reply.reason());
            }
            sagaRepository.save(saga);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
