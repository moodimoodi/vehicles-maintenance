package com.autotech.maintenance.query.messaging;

import com.autotech.maintenance.query.entity.MaintenanceAppointmentViewEntity;
import com.autotech.maintenance.query.messaging.events.*;
import com.autotech.maintenance.query.repository.MaintenanceAppointmentViewRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class MaintenanceEventsListener {

    private final MaintenanceAppointmentViewRepository repository;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "maintenance-events", groupId = "maintenance-query-service-es")
    @Transactional
    public void onEvent(String rawJson) {
        try {
            JsonNode tree = objectMapper.readTree(rawJson);
            String eventType = tree.get("eventType").asText();
            JsonNode payloadNode = tree.get("payload");
            log.info("Received maintenance event type={}", eventType);

            switch (eventType) {
                case "MaintenanceScheduledEvent" -> handleScheduled(payloadNode);
                case "MaintenanceRescheduledEvent" -> handleRescheduled(payloadNode);
                case "MaintenanceStatusChangedEvent" -> handleStatusChanged(payloadNode);
                case "MaintenanceCancelledEvent" -> handleCancelled(payloadNode);
                case "MaintenanceCompletedEvent" -> handleCompleted(payloadNode);
                default -> log.warn("Unknown eventType={}", eventType);
            }
        } catch (Exception e) {
            log.error("Error while processing maintenance event", e);
            throw new RuntimeException(e);
        }
    }

    private void handleScheduled(JsonNode payloadNode) throws Exception {
        MaintenanceScheduledEvent event =
                objectMapper.treeToValue(payloadNode, MaintenanceScheduledEvent.class);

        MaintenanceAppointmentViewEntity view = new MaintenanceAppointmentViewEntity();
        view.setAppointmentId(event.appointmentId());
        view.setVehicleVin(event.vehicleVin());
        view.setCustomerId(event.customerId());
        view.setWorkshopId(event.workshopId());
        view.setScheduledAt(event.scheduledAt());
        view.setMaintenanceType(event.maintenanceType());
        view.setStatus(event.status());
        view.setLastUpdatedAt(LocalDateTime.now());
        repository.save(view);
    }

    private void handleRescheduled(JsonNode payloadNode) throws Exception {
        MaintenanceRescheduledEvent event =
                objectMapper.treeToValue(payloadNode, MaintenanceRescheduledEvent.class);

        repository.findById(event.appointmentId()).ifPresent(view -> {
            view.setScheduledAt(event.newScheduledAt());
            view.setLastUpdatedAt(LocalDateTime.now());
            repository.save(view);
        });
    }

    private void handleStatusChanged(JsonNode payloadNode) throws Exception {
        MaintenanceStatusChangedEvent event =
                objectMapper.treeToValue(payloadNode, MaintenanceStatusChangedEvent.class);

        repository.findById(event.appointmentId()).ifPresent(view -> {
            view.setStatus(event.newStatus());
            view.setLastUpdatedAt(LocalDateTime.now());
            repository.save(view);
        });
    }

    private void handleCancelled(JsonNode payloadNode) throws Exception {
        MaintenanceCancelledEvent event =
                objectMapper.treeToValue(payloadNode, MaintenanceCancelledEvent.class);

        repository.findById(event.appointmentId()).ifPresent(view -> {
            view.setStatus("CANCELLED");
            view.setLastUpdatedAt(LocalDateTime.now());
            repository.save(view);
        });
    }

    private void handleCompleted(JsonNode payloadNode) throws Exception {
        MaintenanceCompletedEvent event =
                objectMapper.treeToValue(payloadNode, MaintenanceCompletedEvent.class);

        repository.findById(event.appointmentId()).ifPresent(view -> {
            view.setStatus("COMPLETED");
            view.setLastUpdatedAt(LocalDateTime.now());
            repository.save(view);
        });
    }
}
