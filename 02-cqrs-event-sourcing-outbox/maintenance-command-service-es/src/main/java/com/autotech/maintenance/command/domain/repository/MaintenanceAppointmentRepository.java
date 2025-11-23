package com.autotech.maintenance.command.domain.repository;

import com.autotech.maintenance.command.domain.aggregate.MaintenanceAppointmentAgrregate;
import com.autotech.maintenance.command.eventstore.api.MaintenanceEventStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Façade that handles persistence of maintenance and outbox events
 */
@Repository
@RequiredArgsConstructor
public class MaintenanceAppointmentRepository {

    private final MaintenanceEventStore eventStore;

    public MaintenanceAppointmentAgrregate load(String appointmentId) {
        List<Object> history = eventStore.load(appointmentId);
        if (history.isEmpty()) {
            throw new IllegalArgumentException("Maintenance appointment not found: " + appointmentId);
        }
        MaintenanceAppointmentAgrregate agg = new MaintenanceAppointmentAgrregate();
        agg.replay(history);
        return agg;
    }

    public void save(MaintenanceAppointmentAgrregate agg) {
        var events = agg.getUncommittedEvents();
        if (events.isEmpty()) return;
        int expectedVersion = agg.getVersion() - events.size();
        eventStore.save(agg.getAppointmentId(), "MaintenanceAppointment", expectedVersion, events);
        agg.markEventsCommitted();
    }
}
