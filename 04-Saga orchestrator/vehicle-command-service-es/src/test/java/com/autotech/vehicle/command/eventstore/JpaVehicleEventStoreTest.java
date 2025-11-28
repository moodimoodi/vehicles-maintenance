package com.autotech.vehicle.command.eventstore;

import com.autotech.vehicle.command.domain.event.*;
import com.autotech.vehicle.command.eventstore.jpa.JpaVehicleEventStore;
import com.autotech.vehicle.command.eventstore.jpa.entity.VehicleEventEntity;
import com.autotech.vehicle.command.eventstore.jpa.entity.VehicleOutboxEventEntity;
import com.autotech.vehicle.command.eventstore.jpa.repository.VehicleEventRepository;
import com.autotech.vehicle.command.eventstore.jpa.repository.VehicleOutboxRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour JpaVehicleEventStore.
 *
 * Objectif :
 *  - Vérifier que la méthode save() :
 *      * persiste bien les VehicleEventEntity avec les bonnes données
 *      * crée les VehicleOutboxEventEntity correspondants (pattern Outbox)
 *  - Vérifier que la méthode load() :
 *      * reconstruit les bons événements de domaine à partir du JSON stocké
 */
class JpaVehicleEventStoreTest {

    private VehicleEventRepository eventRepository;
    private VehicleOutboxRepository outboxRepository;
    private ObjectMapper objectMapper;

    private JpaVehicleEventStore eventStore;

    @BeforeEach
    void setUp() {
        // On mocke les repositories JPA (on ne teste pas la DB ici, juste la logique)
        eventRepository = mock(VehicleEventRepository.class);
        outboxRepository = mock(VehicleOutboxRepository.class);

        // On utilise un vrai ObjectMapper pour sérialiser/désérialiser le JSON
        objectMapper = new ObjectMapper();

        eventStore = new JpaVehicleEventStore(eventRepository, outboxRepository, objectMapper);
    }

    @Test
    void save_shouldPersistEventsAndOutboxRows() throws Exception {
        // GIVEN
        // Un événement de domaine à sauvegarder
        VehicleRegisteredEvent event1 = new VehicleRegisteredEvent(
                "VIN-123",
                "Toyota",
                "Corolla",
                2020,
                30000,
                "ACTIVE",
                "CUST-1"
        );

        VehicleStatusChangedEvent event2 = new VehicleStatusChangedEvent(
                "VIN-123",
                "ACTIVE",
                "INACTIVE"
        );

        List<Object> events = List.of(event1, event2);

        // WHEN
        // expectedVersion = 0 → on s’attend à sauvegarder les versions 1 et 2
        eventStore.save("VIN-123", "Vehicle", 0, events);

        // THEN
        // 1️) Vérifier les VehicleEventEntity persistés
        ArgumentCaptor<VehicleEventEntity> eventCaptor =
                ArgumentCaptor.forClass(VehicleEventEntity.class);
        verify(eventRepository, times(2)).save(eventCaptor.capture());

        List<VehicleEventEntity> savedEvents = eventCaptor.getAllValues();
        assertEquals(2, savedEvents.size());

        VehicleEventEntity e1 = savedEvents.get(0);
        assertEquals("VIN-123", e1.getAggregateId());
        assertEquals("Vehicle", e1.getAggregateType());
        assertEquals(1, e1.getVersion()); // expectedVersion + 1
        assertEquals("VehicleRegisteredEvent", e1.getEventType());

        // On vérifie que le payload JSON contient bien la marque et le VIN
        JsonNode payload1 = objectMapper.readTree(e1.getPayload());
        assertEquals("VIN-123", payload1.get("vin").asText());
        assertEquals("Toyota", payload1.get("brand").asText());

        VehicleEventEntity e2 = savedEvents.get(1);
        assertEquals(2, e2.getVersion()); // expectedVersion + 2
        assertEquals("VehicleStatusChangedEvent", e2.getEventType());

        JsonNode payload2 = objectMapper.readTree(e2.getPayload());
        assertEquals("INACTIVE", payload2.get("newStatus").asText());

        // 2️) Vérifier les VehicleOutboxEventEntity générés
        ArgumentCaptor<VehicleOutboxEventEntity> outboxCaptor =
                ArgumentCaptor.forClass(VehicleOutboxEventEntity.class);
        verify(outboxRepository, times(2)).save(outboxCaptor.capture());

        List<VehicleOutboxEventEntity> outboxEvents = outboxCaptor.getAllValues();
        assertEquals(2, outboxEvents.size());

        VehicleOutboxEventEntity o1 = outboxEvents.get(0);
        assertEquals("VIN-123", o1.getAggregateId());
        assertEquals("Vehicle", o1.getAggregateType());
        assertEquals("VehicleRegisteredEvent", o1.getEventType());
        assertEquals("vehicle-events", o1.getTopic());
        assertEquals("PENDING", o1.getStatus());
        assertNotNull(o1.getCreatedAt());

        // Vérifier que le payload de l’outbox est un "envelope" JSON
        JsonNode envelope1 = objectMapper.readTree(o1.getPayload());
        assertEquals("VehicleRegisteredEvent", envelope1.get("eventType").asText());
        assertEquals("VIN-123", envelope1.get("aggregateId").asText());
        assertTrue(envelope1.has("payload")); // le vrai événement est dans payload
    }

    @Test
    void load_shouldRebuildDomainEventsFromStoredEntities() throws Exception {
        // GIVEN
        // On simule deux lignes dans la table vehicle_events
        VehicleRegisteredEvent domainEvent = new VehicleRegisteredEvent(
                "VIN-123",
                "Toyota",
                "Corolla",
                2020,
                30000,
                "ACTIVE",
                "CUST-1"
        );
        String json1 = objectMapper.writeValueAsString(domainEvent);

        VehicleEventEntity entity1 = new VehicleEventEntity();
        entity1.setAggregateId("VIN-123");
        entity1.setAggregateType("Vehicle");
        entity1.setVersion(1);
        entity1.setEventType("VehicleRegisteredEvent");
        entity1.setPayload(json1);
        entity1.setOccurredAt(Instant.now());

        VehicleStatusChangedEvent domainEvent2 =
                new VehicleStatusChangedEvent("VIN-123", "ACTIVE", "INACTIVE");
        String json2 = objectMapper.writeValueAsString(domainEvent2);

        VehicleEventEntity entity2 = new VehicleEventEntity();
        entity2.setAggregateId("VIN-123");
        entity2.setAggregateType("Vehicle");
        entity2.setVersion(2);
        entity2.setEventType("VehicleStatusChangedEvent");
        entity2.setPayload(json2);
        entity2.setOccurredAt(Instant.now());

        when(eventRepository.findByAggregateIdOrderByVersionAsc("VIN-123"))
                .thenReturn(List.of(entity1, entity2));

        // WHEN
        List<Object> events = eventStore.load("VIN-123");

        // THEN
        assertEquals(2, events.size());
        assertTrue(events.get(0) instanceof VehicleRegisteredEvent);
        assertTrue(events.get(1) instanceof VehicleStatusChangedEvent);

        VehicleRegisteredEvent e1 = (VehicleRegisteredEvent) events.get(0);
        assertEquals("VIN-123", e1.vin());
        assertEquals("Toyota", e1.brand());

        VehicleStatusChangedEvent e2 = (VehicleStatusChangedEvent) events.get(1);
        assertEquals("INACTIVE", e2.newStatus());
    }

    @Test
    void load_shouldThrowExceptionForUnknownEventType() {
        // GIVEN
        VehicleEventEntity unknown = new VehicleEventEntity();
        unknown.setAggregateId("VIN-123");
        unknown.setAggregateType("Vehicle");
        unknown.setVersion(1);
        unknown.setEventType("UnknownEventType");
        unknown.setPayload("{\"foo\":\"bar\"}");
        unknown.setOccurredAt(Instant.now());

        when(eventRepository.findByAggregateIdOrderByVersionAsc("VIN-123"))
                .thenReturn(List.of(unknown));

        // WHEN + THEN
        // On s’attend à une IllegalArgumentException car l’eventType n’est pas supporté
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> eventStore.load("VIN-123"));

        assertTrue(ex.getCause() instanceof IllegalArgumentException);
        assertTrue(ex.getCause().getMessage().contains("Unknown event type"));
    }
}
