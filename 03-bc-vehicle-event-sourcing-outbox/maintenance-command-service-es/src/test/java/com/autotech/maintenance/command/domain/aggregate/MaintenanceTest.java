package com.autotech.maintenance.command.domain.aggregate;

import com.autotech.maintenance.command.constants.MaintenanceConstants;
import com.autotech.maintenance.command.domain.events.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires de l'aggregate MaintenanceAppointmentAgrregate.
 *
 * On vérifie :
 * - la création (scheduleNew)
 * - le rescheduling
 * - le changement de statut
 * - l’annulation
 * - la complétion
 * - la replay() à partir d’un historique d’événements
 */
class MaintenanceAppointmentAgrregateTest {

    @Test
    void scheduleNew_shouldCreateAggregateWithScheduledStatusAndOneEvent() {
        // GIVEN
        String appointmentId = "APP-001";
        String vin = "VIN-123";
        String customerId = "CUST-1";
        String workshopId = "WKS-1";
        LocalDateTime scheduledAt = LocalDateTime.now().plusDays(1);
        String maintenanceType = "OIL_CHANGE";

        // WHEN
        MaintenanceAppointmentAgrregate agg = MaintenanceAppointmentAgrregate.scheduleNew(
                appointmentId,
                vin,
                customerId,
                workshopId,
                scheduledAt,
                maintenanceType
        );

        // THEN: état courant
        assertEquals(appointmentId, agg.getAppointmentId());
        assertEquals(vin, agg.getVehicleVin());
        assertEquals(customerId, agg.getCustomerId());
        assertEquals(workshopId, agg.getWorkshopId());
        assertEquals(scheduledAt, agg.getScheduledAt());
        assertEquals(maintenanceType, agg.getMaintenanceType());
        assertEquals(MaintenanceConstants.STATUS_SCHEDULED, agg.getStatus());

        // THEN: un seul événement SCHEDULED dans la liste des events non committés
        List<Object> events = agg.getUncommittedEvents();
        assertEquals(1, events.size());
        assertTrue(events.get(0) instanceof MaintenanceScheduledEvent);

        MaintenanceScheduledEvent e = (MaintenanceScheduledEvent) events.get(0);
        assertEquals(appointmentId, e.appointmentId());
        assertEquals(vin, e.vehicleVin());
        assertEquals(MaintenanceConstants.STATUS_SCHEDULED, e.status());
    }

    @Test
    void reschedule_shouldProduceEventAndUpdateScheduledAt_whenNotCancelledOrCompleted() {
        // GIVEN: un rendez-vous planifié
        LocalDateTime initialDate = LocalDateTime.now().plusDays(1);
        MaintenanceAppointmentAgrregate agg = MaintenanceAppointmentAgrregate.scheduleNew(
                "APP-001",
                "VIN-123",
                "CUST-1",
                "WKS-1",
                initialDate,
                "OIL_CHANGE"
        );
        agg.markEventsCommitted(); // on vide la liste des events pour ne tester que reschedule()

        LocalDateTime newDateTime = initialDate.plusDays(2);

        // WHEN
        agg.reschedule(newDateTime);

        // THEN: l’état doit être mis à jour
        assertEquals(newDateTime, agg.getScheduledAt());

        // THEN: un seul événement MaintenanceRescheduledEvent
        List<Object> events = agg.getUncommittedEvents();
        assertEquals(1, events.size());
        assertTrue(events.get(0) instanceof MaintenanceRescheduledEvent);
        MaintenanceRescheduledEvent e = (MaintenanceRescheduledEvent) events.get(0);
        assertEquals(initialDate, e.oldScheduledAt());
        assertEquals(newDateTime, e.newScheduledAt());
    }

    @Test
    void reschedule_shouldThrowWhenCancelled() {
        // GIVEN: rendez-vous planifié puis annulé
        MaintenanceAppointmentAgrregate agg = MaintenanceAppointmentAgrregate.scheduleNew(
                "APP-001",
                "VIN-123",
                "CUST-1",
                "WKS-1",
                LocalDateTime.now().plusDays(1),
                "OIL_CHANGE"
        );
        agg.markEventsCommitted();
        agg.cancel("Customer request");
        agg.markEventsCommitted();

        // WHEN + THEN: reschedule doit lever une IllegalStateException
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> agg.reschedule(LocalDateTime.now().plusDays(5))
        );
        assertEquals("Cannot reschedule cancelled or completed maintenance", ex.getMessage());
    }

    @Test
    void reschedule_shouldThrowWhenCompleted() {
        // GIVEN: rendez-vous planifié, mis IN_PROGRESS puis complété
        MaintenanceAppointmentAgrregate agg = MaintenanceAppointmentAgrregate.scheduleNew(
                "APP-001",
                "VIN-123",
                "CUST-1",
                "WKS-1",
                LocalDateTime.now().plusDays(1),
                "OIL_CHANGE"
        );
        agg.markEventsCommitted();
        // on passe à IN_PROGRESS puis COMPLETE via les méthodes de domaine
        agg.changeStatus(MaintenanceConstants.STATUS_IN_PROGRESS);
        agg.markEventsCommitted();
        agg.complete(LocalDateTime.now());
        agg.markEventsCommitted();

        // WHEN + THEN
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> agg.reschedule(LocalDateTime.now().plusDays(5))
        );
        assertEquals("Cannot reschedule cancelled or completed maintenance", ex.getMessage());
    }

    @Test
    void changeStatus_shouldProduceEventWhenStatusChanges() {
        // GIVEN
        MaintenanceAppointmentAgrregate agg = MaintenanceAppointmentAgrregate.scheduleNew(
                "APP-001",
                "VIN-123",
                "CUST-1",
                "WKS-1",
                LocalDateTime.now().plusDays(1),
                "OIL_CHANGE"
        );
        agg.markEventsCommitted();

        // WHEN
        agg.changeStatus(MaintenanceConstants.STATUS_IN_PROGRESS);

        // THEN
        assertEquals(MaintenanceConstants.STATUS_IN_PROGRESS, agg.getStatus());

        List<Object> events = agg.getUncommittedEvents();
        assertEquals(1, events.size());
        assertTrue(events.get(0) instanceof MaintenanceStatusChangedEvent);

        MaintenanceStatusChangedEvent e = (MaintenanceStatusChangedEvent) events.get(0);
        assertEquals(MaintenanceConstants.STATUS_SCHEDULED, e.oldStatus());
        assertEquals(MaintenanceConstants.STATUS_IN_PROGRESS, e.newStatus());
    }

    @Test
    void changeStatus_shouldNotProduceEventWhenStatusIsSame() {
        // GIVEN
        MaintenanceAppointmentAgrregate agg = MaintenanceAppointmentAgrregate.scheduleNew(
                "APP-001",
                "VIN-123",
                "CUST-1",
                "WKS-1",
                LocalDateTime.now().plusDays(1),
                "OIL_CHANGE"
        );
        agg.markEventsCommitted();

        // WHEN: on redemande le même statut
        agg.changeStatus(MaintenanceConstants.STATUS_SCHEDULED);

        // THEN: pas d’événement
        assertTrue(agg.getUncommittedEvents().isEmpty());
    }

    @Test
    void cancel_shouldSetStatusCancelledAndProduceEventButBeIdempotent() {
        // GIVEN
        MaintenanceAppointmentAgrregate agg = MaintenanceAppointmentAgrregate.scheduleNew(
                "APP-001",
                "VIN-123",
                "CUST-1",
                "WKS-1",
                LocalDateTime.now().plusDays(1),
                "OIL_CHANGE"
        );
        agg.markEventsCommitted();

        // WHEN: première annulation
        agg.cancel("Customer no-show");

        // THEN: statut CANCELLED + event MaintenanceCancelledEvent
        assertEquals(MaintenanceConstants.STATUS_CANCELLED, agg.getStatus());
        List<Object> events = agg.getUncommittedEvents();
        assertEquals(1, events.size());
        assertTrue(events.get(0) instanceof MaintenanceCancelledEvent);

        agg.markEventsCommitted();

        // WHEN: on essaie d'annuler une deuxième fois (idempotent)
        agg.cancel("Second cancel attempt");
        // THEN: pas de nouvel événement
        assertTrue(agg.getUncommittedEvents().isEmpty());
        assertEquals(MaintenanceConstants.STATUS_CANCELLED, agg.getStatus());
    }

    @Test
    void complete_shouldProduceEventWhenStatusScheduledOrInProgress() {
        // GIVEN: rendez-vous planifié -> IN_PROGRESS
        MaintenanceAppointmentAgrregate agg = MaintenanceAppointmentAgrregate.scheduleNew(
                "APP-001",
                "VIN-123",
                "CUST-1",
                "WKS-1",
                LocalDateTime.now().plusDays(1),
                "OIL_CHANGE"
        );
        agg.markEventsCommitted();
        agg.changeStatus(MaintenanceConstants.STATUS_IN_PROGRESS);
        agg.markEventsCommitted();

        LocalDateTime completedAt = LocalDateTime.now();

        // WHEN
        agg.complete(completedAt);

        // THEN
        assertEquals(MaintenanceConstants.STATUS_COMPLETED, agg.getStatus());
        List<Object> events = agg.getUncommittedEvents();
        assertEquals(1, events.size());
        assertTrue(events.get(0) instanceof MaintenanceCompletedEvent);

        MaintenanceCompletedEvent e = (MaintenanceCompletedEvent) events.get(0);
        assertEquals("APP-001", e.appointmentId());
        assertEquals(completedAt, e.completedAt());
    }

    @Test
    void complete_shouldThrowIfStatusIsNotScheduledOrInProgress() {
        // GIVEN: rendez-vous avec un statut non valide pour la complétion
        MaintenanceAppointmentAgrregate agg = MaintenanceAppointmentAgrregate.scheduleNew(
                "APP-001",
                "VIN-123",
                "CUST-1",
                "WKS-1",
                LocalDateTime.now().plusDays(1),
                "OIL_CHANGE"
        );
        agg.markEventsCommitted();

        // On force un statut exotique via changeStatus()
        agg.changeStatus("DELAYED");
        agg.markEventsCommitted();

        // WHEN + THEN
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> agg.complete(LocalDateTime.now())
        );
        assertTrue(ex.getMessage().startsWith("Cannot complete maintenance with status "));
    }

    @Test
    void replay_shouldRebuildStateFromHistoryAndClearUncommittedEvents() {
        // GIVEN: un historique simulé d'événements
        LocalDateTime scheduledAt = LocalDateTime.now().plusDays(1);
        LocalDateTime completedAt = LocalDateTime.now().plusDays(2);

        MaintenanceScheduledEvent scheduledEvent = new MaintenanceScheduledEvent(
                "APP-001",
                "VIN-123",
                "CUST-1",
                "WKS-1",
                scheduledAt,
                "OIL_CHANGE",
                MaintenanceConstants.STATUS_SCHEDULED
        );
        MaintenanceStatusChangedEvent statusChangedEvent = new MaintenanceStatusChangedEvent(
                "APP-001",
                MaintenanceConstants.STATUS_SCHEDULED,
                MaintenanceConstants.STATUS_IN_PROGRESS
        );
        MaintenanceCompletedEvent completedEvent = new MaintenanceCompletedEvent(
                "APP-001",
                completedAt
        );

        List<Object> history = List.of(
                scheduledEvent,
                statusChangedEvent,
                completedEvent
        );

        MaintenanceAppointmentAgrregate agg = new MaintenanceAppointmentAgrregate();

        // WHEN
        agg.replay(history);

        // THEN: l'état courant doit refléter la séquence d'événements
        assertEquals("APP-001", agg.getAppointmentId());
        assertEquals("VIN-123", agg.getVehicleVin());
        assertEquals("CUST-1", agg.getCustomerId());
        assertEquals("WKS-1", agg.getWorkshopId());
        assertEquals(scheduledAt, agg.getScheduledAt());
        assertEquals("OIL_CHANGE", agg.getMaintenanceType());
        assertEquals(MaintenanceConstants.STATUS_COMPLETED, agg.getStatus());

        // + aucun event non committé après replay()
        assertTrue(agg.getUncommittedEvents().isEmpty());

        // Optionnel : le numéro de version doit correspondre au nombre d'événements appliqués
        assertEquals(3, agg.getVersion());
    }
}
