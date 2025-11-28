package com.autotech.vehicle.query.messaging;

import com.autotech.vehicle.query.entity.VehicleViewEntity;
import com.autotech.vehicle.query.messaging.events.*;
import com.autotech.vehicle.query.repository.VehicleViewRepository;
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
public class VehicleEventsListener {

    private final VehicleViewRepository repository;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "vehicle-events", groupId = "vehicle-query-service")
    @Transactional
    public void onEvent(String rawJson) {
        try {
            JsonNode tree = objectMapper.readTree(rawJson);
            String eventType = tree.get("eventType").asText();
            JsonNode payloadNode = tree.get("payload");
            log.info("Received vehicle event type={}", eventType);

            switch (eventType) {
                case "VehicleRegisteredEvent" -> handleRegistered(payloadNode);
                case "VehicleDetailsUpdatedEvent" -> handleDetailsUpdated(payloadNode);
                case "VehicleStatusChangedEvent" -> handleStatusChanged(payloadNode);
                case "VehicleOwnerAssignedEvent" -> handleOwnerAssigned(payloadNode);
                case "VehicleOwnerUnassignedEvent" -> handleOwnerUnassigned(payloadNode);
                default -> log.warn("Unknown eventType={}", eventType);
            }
        } catch (Exception e) {
            log.error("Error while processing vehicle event", e);
            throw new RuntimeException(e);
        }
    }

    private void handleRegistered(JsonNode payloadNode) throws Exception {
        VehicleRegisteredEvent event =
                objectMapper.treeToValue(payloadNode, VehicleRegisteredEvent.class);

        VehicleViewEntity view = new VehicleViewEntity();
        view.setVin(event.vin());
        view.setBrand(event.brand());
        view.setModel(event.model());
        view.setYear(event.year());
        view.setMileage(event.mileage());
        view.setStatus(event.status());
        view.setOwnerId(event.ownerId());
        view.setLastUpdatedAt(LocalDateTime.now());
        repository.save(view);
    }

    private void handleDetailsUpdated(JsonNode payloadNode) throws Exception {
        VehicleDetailsUpdatedEvent event =
                objectMapper.treeToValue(payloadNode, VehicleDetailsUpdatedEvent.class);

        repository.findById(event.vin()).ifPresent(view -> {
            view.setBrand(event.brand());
            view.setModel(event.model());
            view.setYear(event.year());
            view.setMileage(event.mileage());
            view.setLastUpdatedAt(LocalDateTime.now());
            repository.save(view);
        });
    }

    private void handleStatusChanged(JsonNode payloadNode) throws Exception {
        VehicleStatusChangedEvent event =
                objectMapper.treeToValue(payloadNode, VehicleStatusChangedEvent.class);

        repository.findById(event.vin()).ifPresent(view -> {
            view.setStatus(event.newStatus());
            view.setLastUpdatedAt(LocalDateTime.now());
            repository.save(view);
        });
    }

    private void handleOwnerAssigned(JsonNode payloadNode) throws Exception {
        VehicleOwnerAssignedEvent event =
                objectMapper.treeToValue(payloadNode, VehicleOwnerAssignedEvent.class);

        repository.findById(event.vin()).ifPresent(view -> {
            view.setOwnerId(event.ownerId());
            view.setLastUpdatedAt(LocalDateTime.now());
            repository.save(view);
        });
    }

    private void handleOwnerUnassigned(JsonNode payloadNode) throws Exception {
        VehicleOwnerUnassignedEvent event =
                objectMapper.treeToValue(payloadNode, VehicleOwnerUnassignedEvent.class);

        repository.findById(event.vin()).ifPresent(view -> {
            view.setOwnerId(null);
            view.setLastUpdatedAt(LocalDateTime.now());
            repository.save(view);
        });
    }
}
