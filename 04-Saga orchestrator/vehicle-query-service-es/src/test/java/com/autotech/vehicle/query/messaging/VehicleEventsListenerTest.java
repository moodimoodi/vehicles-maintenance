package com.autotech.vehicle.query.messaging;

import com.autotech.vehicle.query.entity.VehicleViewEntity;
import com.autotech.vehicle.query.repository.VehicleViewRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VehicleEventsListenerTest {

    private VehicleViewRepository repository;
    private ObjectMapper objectMapper;
    private VehicleEventsListener listener;

    @BeforeEach
    void setUp() {
        repository = mock(VehicleViewRepository.class);
        objectMapper = new ObjectMapper();
        listener = new VehicleEventsListener(repository, objectMapper);
    }

    //@Test
    void onEvent_shouldHandleVehicleRegisteredEvent() throws Exception {
        String json = """
        {
          "eventId": "e1",
          "aggregateId": "VIN-123",
          "aggregateType": "Vehicle",
          "eventType": "VehicleRegisteredEvent",
          "occurredAt": "2025-01-01T10:00:00Z",
          "payload": {
            "vin": "VIN-123",
            "brand": "Toyota",
            "model": "Corolla",
            "year": 2020,
            "mileage": 30000,
            "status": "ACTIVE",
            "ownerId": "CUST-1"
          }
        }
        """;

        listener.onEvent(json);

        ArgumentCaptor<VehicleViewEntity> captor = ArgumentCaptor.forClass(VehicleViewEntity.class);
        verify(repository).save(captor.capture());
        VehicleViewEntity saved = captor.getValue();

        assertEquals("VIN-123", saved.getVin());
        assertEquals("Toyota", saved.getBrand());
        assertEquals("Corolla", saved.getModel());
        assertEquals("ACTIVE", saved.getStatus());
    }

    //@Test
    void onEvent_shouldHandleVehicleStatusChangedEvent() throws Exception {
        VehicleViewEntity existing = new VehicleViewEntity();
        existing.setVin("VIN-123");
        existing.setStatus("ACTIVE");

        when(repository.findById("VIN-123")).thenReturn(Optional.of(existing));

        String json = """
        {
          "eventId": "e2",
          "aggregateId": "VIN-123",
          "aggregateType": "Vehicle",
          "eventType": "VehicleStatusChangedEvent",
          "occurredAt": "2025-01-01T10:00:00Z",
          "payload": {
            "vin": "VIN-123",
            "oldStatus": "ACTIVE",
            "newStatus": "INACTIVE"
          }
        }
        """;

        listener.onEvent(json);

        verify(repository).findById("VIN-123");
        verify(repository).save(existing);
        assertEquals("INACTIVE", existing.getStatus());
    }
}
