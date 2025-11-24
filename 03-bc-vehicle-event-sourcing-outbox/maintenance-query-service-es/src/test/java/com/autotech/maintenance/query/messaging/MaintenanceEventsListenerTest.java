package com.autotech.maintenance.query.messaging;

import com.autotech.maintenance.query.entity.MaintenanceAppointmentViewEntity;
import com.autotech.maintenance.query.repository.MaintenanceAppointmentViewRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Test du listener Kafka :
 * - vérifie le traitement du JSON
 * - vérifie la modification des vues
 * - simule l'évolution projectionnelle
 */
class MaintenanceEventsListenerTest {

    private MaintenanceAppointmentViewRepository repository;
    private ObjectMapper mapper;
    private MaintenanceEventsListener listener;

    @BeforeEach
    void setup() {
        repository = mock(MaintenanceAppointmentViewRepository.class);
        mapper = new ObjectMapper();
        listener = new MaintenanceEventsListener(repository, mapper);
    }

    //@Test
    void onEvent_shouldCreateProjectionOnMaintenanceScheduledEvent() throws Exception {

        String json = """
        {
          "eventId": "e1",
          "aggregateId": "APP-123",
          "eventType": "MaintenanceScheduledEvent",
          "payload": {
            "appointmentId": "APP-123",
            "vehicleVin": "VIN-111",
            "customerId": "CUST-1",
            "workshopId": "WORK-9",
            "scheduledAt": "2025-01-01T10:00:00",
            "maintenanceType": "OIL_CHANGE",
            "status": "SCHEDULED"
          }
        }
        """;

        listener.onEvent(json);

        ArgumentCaptor<MaintenanceAppointmentViewEntity> captor =
                ArgumentCaptor.forClass(MaintenanceAppointmentViewEntity.class);

        verify(repository).save(captor.capture());

        MaintenanceAppointmentViewEntity saved = captor.getValue();
        assertEquals("APP-123", saved.getAppointmentId());
        assertEquals("SCHEDULED", saved.getStatus());
    }

    //@Test
    void onEvent_shouldUpdateStatusOnStatusChangedEvent() throws Exception {
        MaintenanceAppointmentViewEntity existing = new MaintenanceAppointmentViewEntity();
        existing.setAppointmentId("APP-123");
        existing.setStatus("SCHEDULED");

        when(repository.findById("APP-123"))
                .thenReturn(Optional.of(existing));

        String json = """
        {
          "eventId": "e2",
          "aggregateId": "APP-123",
          "eventType": "MaintenanceStatusChangedEvent",
          "payload": {
            "appointmentId": "APP-123",
            "oldStatus": "SCHEDULED",
            "newStatus": "IN_PROGRESS"
          }
        }
        """;

        listener.onEvent(json);

        verify(repository).save(existing);
        assertEquals("IN_PROGRESS", existing.getStatus());
    }
}
